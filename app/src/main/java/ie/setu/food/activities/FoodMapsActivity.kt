package ie.setu.food.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import ie.setu.food.databinding.ActivityFoodMapsBinding
import ie.setu.food.databinding.ContentFoodMapsBinding
import ie.setu.food.main.MainApp

class FoodMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private lateinit var binding: ActivityFoodMapsBinding
    private lateinit var contentBinding: ContentFoodMapsBinding
    lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFoodMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        contentBinding = ContentFoodMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)

        app = application as MainApp

        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }
        setSupportActionBar(this.binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        app.foods.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.setOnMapClickListener(this)
            map.setOnMarkerClickListener(this)
            map.addMarker(options)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressedDispatcher.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as Long
        val food = app.foods.findById(tag)
        contentBinding.currentTitle.text = food!!.title
        contentBinding.currentDescription.text = food.description
        Picasso.get().load(food.image).into(contentBinding.currentImage)
        contentBinding.cardView.visibility = View.VISIBLE
        return false
    }

    override fun onMapClick(p0: LatLng) {
        contentBinding.cardView.visibility = View.GONE
    }
}