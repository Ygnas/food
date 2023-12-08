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
import ie.setu.food.databinding.FragmentFoodMapBinding
import ie.setu.food.ui.foodlist.FoodListViewModel
import timber.log.Timber

class FoodMapFragment : Fragment(), GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private lateinit var binding: FragmentFoodMapBinding
    private val viewModel: FoodListViewModel by activityViewModels()
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
            Timber.i("Perkrove!!!!!!!!!!!!!!!!!!")
            foods?.forEach {
                Timber.i("krauna")
                if (it.lat != 0.0 || it.lng != 0.0) {
                    val loc = LatLng(it.lat, it.lng)
                    val options = MarkerOptions().title(it.title).position(loc)
                    map.setOnMapClickListener(this)
                    map.setOnMarkerClickListener(this)
                    map.addMarker(options)?.tag = it.id
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
                }
            }
        }
    }

    override fun onMapClick(p0: LatLng) {
        TODO("Not yet implemented")
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        TODO("Not yet implemented")
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