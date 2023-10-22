package ie.setu.food.views.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import ie.setu.food.databinding.ActivityGalleryViewBinding
import ie.setu.food.main.MainApp
import ie.setu.food.models.FoodModel
import ie.setu.food.views.foodlist.FoodListener

class GalleryView : AppCompatActivity(), ImageListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGalleryViewBinding
    private lateinit var presenter: GalleryPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = GalleryPresenter(this)
        app = application as MainApp

        val layoutManager = GridLayoutManager(this,3)
        binding.galleryrecycler.layoutManager = layoutManager
        loadImages()
    }

    private fun loadImages() {
        binding.galleryrecycler.adapter = ImageAdapter(presenter.getImages(), this)
        onRefresh()
    }

    fun onRefresh() {
        binding.galleryrecycler.adapter?.notifyItemRangeChanged(0, presenter.getImages().size)
    }

    override fun onFoodClick(food: FoodModel, position: Int) {
        TODO("Not yet implemented")
    }
}