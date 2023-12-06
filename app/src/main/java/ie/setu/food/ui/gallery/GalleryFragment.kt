package ie.setu.food.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.food.databinding.FragmentGalleryBinding
import ie.setu.food.models.FoodModel
import ie.setu.food.views.gallery.ImageAdapter
import ie.setu.food.views.gallery.ImageListener
import timber.log.Timber

class GalleryFragment : Fragment(), ImageListener {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var viewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[GalleryViewModel::class.java]

        binding.galleryrecycler.layoutManager = LinearLayoutManager(activity)

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
//            binding.donationsNotFound.visibility = View.VISIBLE
        } else {
            Timber.i(foodList.toString())
            binding.galleryrecycler.visibility = View.VISIBLE
//            binding.donationsNotFound.visibility = View.GONE
        }
    }

    override fun onFoodClick(food: FoodModel, position: Int) {
        TODO("Not yet implemented")
    }

}