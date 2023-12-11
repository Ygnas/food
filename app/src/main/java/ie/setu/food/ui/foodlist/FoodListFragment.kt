package ie.setu.food.ui.foodlist

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import ie.setu.food.databinding.FragmentFoodListBinding
import ie.setu.food.models.FoodModel
import ie.setu.food.ui.account.LoggedInViewModel
import ie.setu.food.views.foodlist.FoodAdapter
import ie.setu.food.views.foodlist.FoodListener
import java.util.Date

class FoodListFragment : Fragment(), FoodListener {

    private lateinit var binding: FragmentFoodListBinding
    private val viewModel: FoodListViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodListBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        viewModel.observableFoodList.observe(viewLifecycleOwner) { foods ->
            foods?.let {
                render(foods as ArrayList<FoodModel>)
            }
        }

        viewModel.observableMainFoodList.observe(viewLifecycleOwner) { foods ->
            foods?.let {
                viewModel.search("")
            }
        }
        setButtonListener()
        return binding.root
    }

    private fun render(foodList: ArrayList<FoodModel>) {
        binding.recyclerView.adapter = FoodAdapter(foodList, this)
        if (foodList.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onFoodClick(food: FoodModel, position: Int) {
        findNavController().navigate(FoodListFragmentDirections.actionNavHomeToFoodFragment(food))
    }

    override fun onResume() {
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser != null) {
                viewModel.liveFirebaseUser.value = firebaseUser
                viewModel.load()
            }
        }
    }

    private fun setButtonListener() {
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.search(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText!!)
                return true
            }
        })

        binding.imageButton.setOnClickListener {
            binding.searchView.setQuery("", false)
            binding.searchView.isIconified = true
            filterDate()
        }

        binding.filterChip.setOnClickListener {
            binding.filterChip.visibility = View.GONE
            viewModel.filterByDate("")
        }
    }

    private fun filterDate() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Filter By Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener { date ->
            val selectedDate = Date(date)
            val formattedDate = SimpleDateFormat.getDateInstance().format(selectedDate)
            viewModel.filterByDate(formattedDate)
            setChip(formattedDate)
        }
        datePicker.show(childFragmentManager, "")
    }

    private fun setChip(text: String) {
        binding.filterChip.text = "Filtering By Date: $text"
        binding.filterChip.visibility = View.VISIBLE
    }
}