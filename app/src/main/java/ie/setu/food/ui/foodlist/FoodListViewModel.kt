package ie.setu.food.ui.foodlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.food.firebase.FirebaseAuthentication
import ie.setu.food.firebase.FirebaseDB
import ie.setu.food.models.FoodModel

class FoodListViewModel : ViewModel() {
    private val foodList = MutableLiveData<List<FoodModel>>()
    val observableFoodList: LiveData<List<FoodModel>>
        get() = foodList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init {
//        FirebaseDB.create(liveFirebaseUser)
        load()
    }

    fun load() {
        FirebaseDB.findAll(foodList)
    }
}