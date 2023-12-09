package ie.setu.food.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import ie.setu.food.databinding.FragmentFoodMapBinding
import ie.setu.food.firebase.FirebaseDB
import ie.setu.food.firebase.FirebaseStorage
import ie.setu.food.ui.foodlist.FoodListViewModel
import timber.log.Timber

class FoodMapFragment : Fragment(), GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private lateinit var binding: FragmentFoodMapBinding
    private val viewModel: FoodListViewModel by activityViewModels()
    private val viewModelFood: FoodViewModel by activityViewModels()
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodMapBinding.inflate(layoutInflater)
        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync {
            map = it
            configureMap()
        }

        return binding.root
    }

    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        viewModel.observableFoodList.observe(viewLifecycleOwner) { foods ->
            foods?.forEach {
                if (it.lat != 0.0 || it.lng != 0.0) {
                    val loc = LatLng(it.lat, it.lng)
                    val options = MarkerOptions().title(it.title).position(loc)
                    map.setOnMapClickListener(this)
                    map.setOnMarkerClickListener(this)
                    map.addMarker(options)?.tag = it.uid
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
                }
            }
        }
    }

    override fun onMapClick(p0: LatLng) {
        binding.cardView.visibility = View.GONE
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as String
        val food = viewModelFood.findById(tag,viewModel.observableFoodList.value!!)
        binding.currentTitle.text = food!!.title
        binding.currentDescription.text = food.description
        FirebaseStorage.loadImageFromFirebase(tag, binding.currentImage, 200)
        binding.cardView.visibility = View.VISIBLE
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

}