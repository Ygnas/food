package ie.setu.food.ui.foodlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.food.databinding.FragmentFoodListBinding
import ie.setu.food.models.FoodModel
import ie.setu.food.views.foodlist.FoodAdapter
import ie.setu.food.views.foodlist.FoodListener
import timber.log.Timber.i

class FoodListFragment : Fragment(), FoodListener {

    private lateinit var binding: FragmentFoodListBinding
    private lateinit var viewModel: FoodListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodListBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[FoodListViewModel::class.java]
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
//            binding.donationsNotFound.visibility = View.VISIBLE
        } else {
            i(foodList.toString())
            binding.recyclerView.visibility = View.VISIBLE
//            binding.donationsNotFound.visibility = View.GONE
        }
    }

    override fun onFoodClick(food: FoodModel, position: Int) {
        val action = FoodListFragmentDirections.actionNavHomeToFoodFragment(food)
        findNavController().navigate(action)
    }
}