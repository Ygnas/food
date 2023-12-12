package ie.setu.food.views.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import ie.setu.food.adapters.ImageAdapter
import ie.setu.food.adapters.ImageListener
import ie.setu.food.databinding.ActivityGalleryViewBinding
import ie.setu.food.main.MainApp
import ie.setu.food.models.FoodModel

class GalleryView : AppCompatActivity(), ImageListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGalleryViewBinding
    private lateinit var presenter: GalleryPresenter
    private var position: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbargallery.title = title
        presenter = GalleryPresenter(this)
        app = application as MainApp
        setSupportActionBar(this.binding.toolbargallery).apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        val layoutManager = GridLayoutManager(this,3)
        binding.galleryrecycler.layoutManager = layoutManager
        loadImages()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressedDispatcher.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadImages() {
        binding.galleryrecycler.adapter = ImageAdapter(presenter.getImages(), this)
        onRefresh()
    }

    fun onRefresh() {
        binding.galleryrecycler.adapter?.notifyItemRangeChanged(0, presenter.getImages().size)
    }

    override fun onFoodClick(food: FoodModel, position: Int) {
        this.position = position
        presenter.doEditFood(food, this.position)
    }
}