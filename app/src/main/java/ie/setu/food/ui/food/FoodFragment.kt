package ie.setu.food.ui.food

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import ie.setu.food.R
import ie.setu.food.databinding.FragmentFoodBinding
import ie.setu.food.firebase.FirebaseDB
import ie.setu.food.firebase.FirebaseStorage
import ie.setu.food.helpers.showImagePicker
import ie.setu.food.models.FoodModel
import ie.setu.food.models.FoodType
import ie.setu.food.ui.account.LoggedInViewModel
import ie.setu.food.views.camera.CameraView
import java.util.Date
import java.util.Locale

class FoodFragment : Fragment() {

    private val args by navArgs<FoodFragmentArgs>()
    private lateinit var binding: FragmentFoodBinding
    private lateinit var viewModel: FoodViewModel
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageUri: Uri
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var loc: LatLng

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[FoodViewModel::class.java]

        val spinner = binding.spinner
        spinner.adapter =
            ArrayAdapter(
                requireContext(), android.R.layout.simple_spinner_item,
                FoodType.entries.toTypedArray()
            )

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        setLocation()
        cameraImageIntent()

        args.food?.let { food ->
            binding.btnAdd.text = getString(R.string.save_food)
            with(food) {
                binding.foodTitle.setText(title)
                binding.foodDescription.setText(description)
                binding.editTextDate.setText(date)
                binding.spinner.setSelection(foodType.ordinal)
                uid?.let { FirebaseStorage.loadImageFromFirebase(it, binding.foodImage, 450) }
                if (date.isEmpty()) {
                    binding.editTextDate.setText(
                        SimpleDateFormat.getDateInstance()
                            .format(MaterialDatePicker.todayInUtcMilliseconds())
                    )
                }
            }
        }
        if (binding.editTextDate.text.isNullOrEmpty()) {
            binding.editTextDate.setText(
                SimpleDateFormat(
                    "dd MMM yyyy",
                    Locale.UK
                ).format(MaterialDatePicker.todayInUtcMilliseconds())
            )
        }

        setButtonListener(binding)
        return binding.root
    }

    private fun setButtonListener(binding: FragmentFoodBinding) {
        binding.btnAdd.setOnClickListener {
            if (binding.foodTitle.text.toString().isEmpty()) {
                Snackbar.make(
                    binding.root,
                    R.string.enter_food_title,
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else {
                val food = FoodModel(
                    title = binding.foodTitle.text.toString(),
                    description = binding.foodDescription.text.toString(),
                    image = Uri.EMPTY,
                    date = binding.editTextDate.text.toString(),
                    foodType = binding.spinner.selectedItem as FoodType,
                    uid = args.food?.uid,
                    lat = loc.latitude,
                    lng = loc.longitude,
                    zoom = 16f
                )
                viewModel.addFood(loggedInViewModel.liveFirebaseUser, food)
                val bitmap: Bitmap
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    FirebaseDB.observableKey.observe(viewLifecycleOwner) { key ->
                        viewModel.uploadImage(key, bitmap)
                    }
                } catch (_: Exception) {
                }
                findNavController().navigateUp()
            }
        }

        binding.editTextDate.setOnClickListener {
            showDate()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher, requireContext())
        }

        binding.buttonCamera.setOnClickListener {
            startCamera()
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
            val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.UK).format(selectedDate)

            binding.editTextDate.setText(formattedDate)
        }
        datePicker.show(childFragmentManager, "tag")
    }

    private fun startCamera() {
        val launcherIntent = Intent(requireContext(), CameraView::class.java)
        cameraIntentLauncher.launch(launcherIntent)
    }

    private fun setLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermissions()
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
//                    args.food?.lat = location.latitude
//                    args.food?.lng = location.longitude
                    loc = LatLng(location.latitude, location.longitude)
                }
            }
    }

    private fun requestLocationPermissions() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {}
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {}
            }
        }
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun cameraImageIntent() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    imageUri = result.data!!.data!!
                    binding.foodImage.setImageURI(imageUri)
                    args.food?.image = imageUri
                }
            }

        cameraIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    imageUri = data?.getParcelableExtra<Uri>("imagePath")!!
                    binding.foodImage.setImageURI(imageUri)
                    args.food?.image = imageUri
                }
            }
    }
}