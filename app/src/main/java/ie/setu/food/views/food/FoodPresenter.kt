package ie.setu.food.views.food

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ie.setu.food.databinding.ActivityFoodBinding
import ie.setu.food.helpers.showImagePicker
import ie.setu.food.main.MainApp
import ie.setu.food.models.FoodModel
import ie.setu.food.models.FoodType
import ie.setu.food.models.Location
import ie.setu.food.views.camera.CameraView
import ie.setu.food.views.editlocation.EditLocationView
import timber.log.Timber

class FoodPresenter(private val view: FoodView) {

    var food = FoodModel()
    var app: MainApp = view.application as MainApp
    var binding: ActivityFoodBinding = ActivityFoodBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var cameraIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false

    init {
        if (view.intent.hasExtra("food_edit")) {
            edit = true
            food = view.intent.extras?.getParcelable("food_edit")!!
            view.showFood(food)
        }
        registerImagePickerCallback()
        registerMapCallback()
        registerCameraCallback()
    }
    fun doAddOrSave(title: String, description: String, date: String, foodType: FoodType) {
        food.title = title
        food.description = description
        food.date = date
        food.foodType = foodType
        if (view.loc.latitude != 0.0 && food.lat == 0.0) {
            food.lat = view.loc.latitude
            food.lng = view.loc.longitude
            food.zoom = 16f
        }
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
//        app.foods.delete(food)
        view.finish()
    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher, view)
    }

    fun doSetLocation(lat : Double, lng : Double) {
        val location: Location = if (lat == 0.0 && lng == 0.0) {
            Location(52.245696, -7.139102, 15f)
        } else {
            Location(lat, lng, 15f)
        }
        if (food.zoom != 0f) {
            location.lat = food.lat
            location.lng = food.lng
            location.zoom = food.zoom
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
        launcherIntent.putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun cacheFood(title: String, description: String, date: String, foodType: FoodType) {
        food.title = title
        food.description = description
        food.date = date
        food.foodType = foodType
    }

    fun showCamera() {
        val launcherIntent = Intent(view, CameraView::class.java)
        cameraIntentLauncher.launch(launcherIntent)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            food.image = result.data!!.data!!
                            view.contentResolver.takePersistableUriPermission(food.image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            view.updateImage(food.image)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }            }    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location =
                                result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            food.lat = location.lat
                            food.lng = location.lng
                            food.zoom = location.zoom
                        } // end of if
                    }

                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

    private fun registerCameraCallback() {
        cameraIntentLauncher = view.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val imagePath = data?.getParcelableExtra<Uri>("imagePath")
                if (imagePath != null) {
                    food.image = imagePath
                    view.updateImage(food.image)
                }
            }
        }
    }
}
