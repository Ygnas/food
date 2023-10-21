package ie.setu.food.views.food

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ie.setu.food.databinding.ActivityFoodBinding
import ie.setu.food.helpers.showImagePicker
import ie.setu.food.main.MainApp
import ie.setu.food.models.FoodModel
import ie.setu.food.models.Location
import timber.log.Timber

class FoodPresenter(private val view: FoodView) {

    var food = FoodModel()
    var app: MainApp = view.application as MainApp
    var binding: ActivityFoodBinding = ActivityFoodBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false

    init {
        if (view.intent.hasExtra("placemark_edit")) {
            edit = true
            food = view.intent.extras?.getParcelable("placemark_edit")!!
            view.showPlacemark(food)
        }
        registerMapCallback()
    }
    fun doAddOrSave(title: String, description: String) {
        food.title = title
        food.description = description
        if (edit) {
            app.foods.update(food)
        } else {
            app.foods.create(food)
        }
        view.setResult(RESULT_OK)
        view.finish()
    }
    fun doCancel() {
        view.finish()
    }
    fun doDelete() {
        view.setResult(99)
        app.foods.delete(food)
        view.finish()
    }
    fun doSelectImage() {
        showImagePicker(imageIntentLauncher,view)
    }

    fun cachePlacemark (title: String, description: String) {
        food.title = title;
        food.description = description
    }


    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            food.lat = location.lat
                            food.lng = location.lng
                            food.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }            }    }}