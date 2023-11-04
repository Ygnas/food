package ie.setu.food.views.food

import android.Manifest
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.food.R
import ie.setu.food.databinding.ActivityFoodBinding
import ie.setu.food.models.FoodModel
import ie.setu.food.models.FoodType
import timber.log.Timber
import java.util.Date

class FoodView : AppCompatActivity() {

    private lateinit var binding: ActivityFoodBinding
    private lateinit var presenter: FoodPresenter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var food = FoodModel()
    var loc = Location("food")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (binding.editTextDate.text.isEmpty()) {
            binding.editTextDate.setText(SimpleDateFormat.getDateInstance().format(MaterialDatePicker.todayInUtcMilliseconds()))
        }
        val spinner = binding.spinner
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, FoodType.values())

        presenter = FoodPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheFood(binding.foodTitle.text.toString(), binding.foodDescription.text.toString(),
                binding.editTextDate.text.toString(), binding.spinner.selectedItem as FoodType)
            presenter.doSelectImage()
        }

        binding.foodLocation.setOnClickListener {
            presenter.cacheFood(binding.foodTitle.text.toString(), binding.foodDescription.text.toString(),
                binding.editTextDate.text.toString(), binding.spinner.selectedItem as FoodType)
            presenter.doSetLocation(loc.latitude, loc.longitude)
        }

        binding.btnAdd.setOnClickListener {
            if (binding.foodTitle.text.toString().isEmpty()) {
                Snackbar.make(binding.root, R.string.enter_food_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                presenter.doAddOrSave(
                    binding.foodTitle.text.toString(),
                    binding.foodDescription.text.toString(),
                    binding.editTextDate.text.toString(),
                    binding.spinner.selectedItem as FoodType
                )
            }
        }

        binding.editTextDate.setOnClickListener {
            showDate()
        }

        binding.buttonCamera.setOnClickListener {
            presenter.showCamera()
        }
        requestLocationPermissions()
        setLocation()
    }

    private fun showDate() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener { date ->
            val selectedDate = Date(date)
            val formattedDate = SimpleDateFormat.getDateInstance().format(selectedDate)

            binding.editTextDate.setText(formattedDate)
        }
        datePicker.show(supportFragmentManager, "tag")
    }

    private fun requestLocationPermissions() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {}
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {}
            }
        }
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    private fun setLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermissions()
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                location?.let {
                    loc.latitude = location.latitude
                    loc.longitude = location.longitude
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_food, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        deleteMenu.isVisible = presenter.edit
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showFood(food: FoodModel) {
        binding.foodTitle.setText(food.title)
        binding.foodDescription.setText(food.description)
        binding.editTextDate.setText(food.date)
        binding.btnAdd.setText(R.string.save_food)
        binding.spinner.setSelection(food.foodType.ordinal)
        Picasso.get()
            .load(food.image)
            .into(binding.foodImage)
        if (food.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_food_image)
        }
    }

    fun updateImage(image: Uri) {
        Timber.i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.foodImage)
        binding.chooseImage.setText(R.string.change_food_image)
    }
}

