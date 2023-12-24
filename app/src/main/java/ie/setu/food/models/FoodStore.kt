package ie.setu.food.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface FoodStore {
    fun findById(id: String, foods: List<FoodModel>): FoodModel?
    fun delete(userid: String, id: String)
    fun findAll(foodList: MutableLiveData<List<FoodModel>>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, food: FoodModel)

}