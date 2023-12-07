package ie.setu.food.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.food.firebase.FirebaseDB
import ie.setu.food.models.FoodModel

class GalleryViewModel : ViewModel() {
    private val foodList = MutableLiveData<List<FoodModel>>()
    val observableFoodList: LiveData<List<FoodModel>>
        get() = foodList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init {
        load()
    }

    fun load() {
        try {
            FirebaseDB.findAll(liveFirebaseUser.value?.uid!!, foodList)
        } catch (_: Exception) {
        }

    }
}