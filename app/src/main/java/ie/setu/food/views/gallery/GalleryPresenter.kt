package ie.setu.food.views.gallery

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.setu.food.main.MainApp
import ie.setu.food.models.FoodModel
import ie.setu.food.views.food.FoodView

class GalleryPresenter(val view: GalleryView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private var position: Int = 0

    fun getImages() = app.foods.findAll()

    init {
        registerRefreshCallback()
    }

    fun doEditFood(food: FoodModel, pos: Int) {
        val launcherIntent = Intent(view, FoodView::class.java)
        launcherIntent.putExtra("food_edit", food)
        position = pos
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {  }
    }
}