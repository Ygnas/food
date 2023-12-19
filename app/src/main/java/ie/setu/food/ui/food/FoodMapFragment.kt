package ie.setu.food.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.setu.food.adapters.FoodAdapter
import ie.setu.food.adapters.FoodListener
import ie.setu.food.databinding.FragmentFoodMapBinding
import ie.setu.food.firebase.FirebaseStorage
import ie.setu.food.models.FoodModel
import ie.setu.food.ui.foodlist.FoodListViewModel

class FoodMapFragment : Fragment(), GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener,
    FoodListener {

    private lateinit var binding: FragmentFoodMapBinding
    private val viewModel: FoodListViewModel by activityViewModels()
    private val viewModelFood: FoodViewModel by activityViewModels()
    private lateinit var map: GoogleMap
    private val markerList: MutableList<Marker> = mutableListOf()

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

        binding.mapList.layoutManager = LinearLayoutManager(activity)

        viewModel.observableFoodList.observe(viewLifecycleOwner) { foods ->
            foods?.let {
                render(foods as ArrayList<FoodModel>)
            }
        }

        binding.switch3.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            viewModel.filterFav(b)
            configureMap()
        }

        binding.floatingActionButton.setOnClickListener {
            binding.mapList.visibility =
                if (binding.mapList.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        return binding.root
    }

    private fun render(foodList: ArrayList<FoodModel>) {
        binding.mapList.adapter = FoodAdapter(foodList, this)
    }

    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        viewModel.observableFoodList.observe(viewLifecycleOwner) { foods ->
            clearMarkers()
            foods?.forEach {
                if (it.lat != 0.0 || it.lng != 0.0) {
                    val loc = LatLng(it.lat, it.lng)
                    val options = MarkerOptions().title(it.title).position(loc)
                    map.setOnMapClickListener(this)
                    map.setOnMarkerClickListener(this)
                    val marker = map.addMarker(options)
                    marker?.tag = it.uid
                    markerList.add(marker!!)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
                }
            }
        }
    }

    private fun clearMarkers() {
        for (marker in markerList) {
            marker.remove()
        }
        markerList.clear()

    }

    override fun onMapClick(p0: LatLng) {
        binding.cardView.visibility = View.GONE
        binding.mapList.visibility = View.GONE
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as String
        val food = viewModelFood.findById(tag, viewModel.observableFoodList.value!!)
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

    override fun onFoodClick(food: FoodModel, position: Int) {
        binding.mapList.visibility = View.GONE
        val loc = LatLng(food.lat, food.lng)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, food.zoom))
        binding.currentTitle.text = food.title
        binding.currentDescription.text = food.description
        FirebaseStorage.loadImageFromFirebase(food.uid!!, binding.currentImage, 200)
        binding.cardView.visibility = View.VISIBLE
    }

}