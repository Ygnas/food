package ie.setu.food.ui.food

import android.R
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import ie.setu.food.databinding.FragmentFoodBinding
import ie.setu.food.firebase.FirebaseAuthentication
import ie.setu.food.firebase.FirebaseStorage
import ie.setu.food.helpers.showImagePicker
import ie.setu.food.models.FoodModel
import ie.setu.food.models.FoodType
import java.util.Date

class FoodFragment : Fragment() {

    private val args by navArgs<FoodFragmentArgs>()
    private lateinit var binding: FragmentFoodBinding
    private lateinit var viewModel: FoodViewModel
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[FoodViewModel::class.java]

        val spinner = binding.spinner
        spinner.adapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_item, FoodType.values())

        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    imageUri = result.data!!.data!!
                    binding.foodImage.setImageURI(imageUri)
                }
            }

        with(args.food) {
            binding.foodTitle.setText(title)
            binding.foodDescription.setText(description)
            binding.editTextDate.setText(date)
            binding.spinner.setSelection(foodType.ordinal)
            FirebaseStorage.loadImageFromFirebase(uid!!, binding.foodImage)
            if (date.isEmpty()) {
                binding.editTextDate.setText(
                    SimpleDateFormat.getDateInstance()
                        .format(MaterialDatePicker.todayInUtcMilliseconds())
                )
            }
        }
        setButtonListener(binding)
        return binding.root
    }

    private fun setButtonListener(binding: FragmentFoodBinding) {
        binding.btnAdd.setOnClickListener {
            if (binding.foodTitle.text.toString().isEmpty()) {
                Snackbar.make(
                    binding.root,
                    ie.setu.food.R.string.enter_food_title,
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else {
                val food = FoodModel(
                    title = binding.foodTitle.text.toString(),
                    description = binding.foodDescription.text.toString(),
                    image = Uri.EMPTY,
                    date = binding.editTextDate.text.toString(),
                    foodType = binding.spinner.selectedItem as FoodType
                )
                val auth = FirebaseAuthentication(Application())
                val liveFirebaseUser: MutableLiveData<FirebaseUser> = auth.liveFirebaseUser

                val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                viewModel.uploadImage("-Nl-9H6ndc24GemlZYO9", bitmap)
                viewModel.addFood(liveFirebaseUser, food)
            }
        }

        binding.editTextDate.setOnClickListener {
            showDate()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher, requireContext())
        }
    }

    private fun showDate() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener { date ->
            val selectedDate = Date(date)
            val formattedDate = SimpleDateFormat.getDateInstance().format(selectedDate)

            binding.editTextDate.setText(formattedDate)
        }
        datePicker.show(childFragmentManager, "tag")
    }
}