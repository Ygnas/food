package ie.setu.food.views.foodlist

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.datepicker.MaterialDatePicker
import ie.setu.food.activities.FoodMapsActivity
import ie.setu.food.databinding.ActivityFoodListBinding
import ie.setu.food.main.MainApp
import ie.setu.food.models.FoodModel
import ie.setu.food.views.account.LoginView
import ie.setu.food.views.food.FoodView
import ie.setu.food.views.gallery.GalleryView
import java.util.Date

class FoodListPresenter(val view: FoodListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var loginIntentLauncher: ActivityResultLauncher<Intent>
    var binding: ActivityFoodListBinding = ActivityFoodListBinding.inflate(view.layoutInflater)
    private var position: Int = 0

    init {
        app = view.application as MainApp
        registerMapCallback()
        registerRefreshCallback()
        registerGalleryCallback()
        registerLoginCallback()
    }

    fun getFoods() = app.foods.findAll()

    fun doAddFood() {
        val launcherIntent = Intent(view, FoodView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doEditFood(food: FoodModel, pos: Int) {
        val launcherIntent = Intent(view, FoodView::class.java)
        launcherIntent.putExtra("food_edit", food)
        position = pos
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doShowFoodsMap() {
        val launcherIntent = Intent(view, FoodMapsActivity::class.java)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun doShowGallery() {
        val launcherIntent = Intent(view, GalleryView::class.java)
        galleryIntentLauncher.launch(launcherIntent)
    }

    fun doShowLogin() {
        val launcherIntent = Intent(view, LoginView::class.java)
        loginIntentLauncher.launch(launcherIntent)
    }

    fun filterDate() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Filter By Date")
                .build()

        datePicker.addOnPositiveButtonClickListener { date ->
            val selectedDate = Date(date)
            val formattedDate = SimpleDateFormat.getDateInstance().format(selectedDate)
//            view.filterFoodsByDate(formattedDate)
            view.setChip(formattedDate)
        }
        datePicker.show(view.supportFragmentManager, "")
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
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }

    private fun registerGalleryCallback() {
        galleryIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }

    private fun registerLoginCallback() {
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }
}
