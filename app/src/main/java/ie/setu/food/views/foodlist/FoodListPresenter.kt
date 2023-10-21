package ie.setu.food.views.foodlist

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.setu.food.main.MainApp
import ie.setu.food.models.FoodModel
import ie.setu.food.views.food.FoodView

class FoodListPresenter(val view: FoodListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private var position: Int = 0

    init {
        app = view.application as MainApp
        registerMapCallback()
        registerRefreshCallback()
    }

    fun getPlacemarks() = app.foods.findAll()

    fun doAddPlacemark() {
        val launcherIntent = Intent(view, FoodView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doEditPlacemark(food: FoodModel, pos: Int) {
        val launcherIntent = Intent(view, FoodView::class.java)
        launcherIntent.putExtra("placemark_edit", food)
        position = pos
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) view.onRefresh()
                else // Deleting
                    if (it.resultCode == 99) view.onDelete(position)
            }
    }
    private fun registerMapCallback() {
        mapIntentLauncher = view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {  }
    }
}