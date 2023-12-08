package ie.setu.food.ui.foodlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.food.databinding.FragmentFoodListBinding
import ie.setu.food.models.FoodModel
import ie.setu.food.ui.account.LoggedInViewModel
import ie.setu.food.views.foodlist.FoodAdapter
import ie.setu.food.views.foodlist.FoodListener

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
}