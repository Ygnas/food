package ie.setu.food.ui.food

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.food.firebase.FirebaseDB
import ie.setu.food.models.FoodModel

class FoodViewModel : ViewModel() {

    fun addFood(
        firebaseUser: MutableLiveData<FirebaseUser>,
        food: FoodModel
    ) {
        FirebaseDB.create(firebaseUser, food)
    }
}