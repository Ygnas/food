package ie.setu.food.ui.foodlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.food.firebase.FirebaseDB
import ie.setu.food.firebase.FirebaseStorage
import ie.setu.food.models.FoodModel

class FoodListViewModel : ViewModel() {
    private val foodList = MutableLiveData<List<FoodModel>>()
    private var filteredFoodList = MutableLiveData<List<FoodModel>>()
    val observableFoodList: LiveData<List<FoodModel>>
        get() = filteredFoodList

    val observableMainFoodList: LiveData<List<FoodModel>>
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

    fun search(query: String) {
        filteredFoodList.postValue(foodList.value?.filter {
            it.title.contains(query, ignoreCase = true) || it.description.contains(
                query,
                ignoreCase = true
            ) || it.foodType.toString().contains(query, ignoreCase = true)
        })
    }

    fun filterByDate(date: String?) {
        if (date!!.isNotBlank()) {
            filteredFoodList.postValue(foodList.value?.filter {
                it.date == date
            })
        } else {
            search("")
        }
    }

    fun filterFav(bool: Boolean) {
        if (bool) {
            filteredFoodList.postValue(foodList.value?.filter {
                it.fav
            })
        } else {
            filteredFoodList.postValue(foodList.value)
        }
    }

    fun setFav(food: FoodModel) {
        FirebaseDB.setFavoriteStatus(liveFirebaseUser, food)
    }

    fun delete(userid: String, id: String) {
        try {
            FirebaseDB.delete(userid, id)
        } catch (_: Exception) {
        }
    }

    fun deleteImage(uid: String) {
        FirebaseStorage.deleteImageFromFirebase(uid)
    }
}