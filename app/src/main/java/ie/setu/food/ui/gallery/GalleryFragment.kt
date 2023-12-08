package ie.setu.food.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.food.databinding.FragmentGalleryBinding
import ie.setu.food.models.FoodModel
import ie.setu.food.ui.account.LoggedInViewModel
import ie.setu.food.views.gallery.ImageAdapter
import ie.setu.food.views.gallery.ImageListener
import timber.log.Timber

class GalleryFragment : Fragment(), ImageListener {

    private lateinit var binding: FragmentGalleryBinding
    private val viewModel: GalleryViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(layoutInflater)

        val layoutManager = GridLayoutManager(requireContext(),3)
        binding.galleryrecycler.layoutManager = layoutManager

        viewModel.observableFoodList.observe(viewLifecycleOwner) { foods ->
            foods?.let {
                render(foods as ArrayList<FoodModel>)
            }
        }
        return binding.root
    }

    private fun render(foodList: ArrayList<FoodModel>) {
        binding.galleryrecycler.adapter = ImageAdapter(foodList, this)
        if (foodList.isEmpty()) {
            binding.galleryrecycler.visibility = View.GONE
        } else {
            Timber.i(foodList.toString())
            binding.galleryrecycler.visibility = View.VISIBLE
        }
    }

    override fun onFoodClick(food: FoodModel, position: Int) {
        TODO("Not yet implemented")
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