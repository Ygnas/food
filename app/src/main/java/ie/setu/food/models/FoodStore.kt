package ie.setu.food.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface FoodStore {
    fun findAll(): List<FoodModel>
    fun findById(id: Long): FoodModel?
    fun findById(id: String, foods: List<FoodModel>): FoodModel?
    fun create(food: FoodModel)
    fun update(food: FoodModel)
    fun delete(userid: String, id: String)

    fun findAll(foodList: MutableLiveData<List<FoodModel>>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, food: FoodModel)

}