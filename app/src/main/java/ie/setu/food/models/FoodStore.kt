package ie.setu.food.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface FoodStore {
    fun findAll(): List<FoodModel>
    fun findById(id:Long): FoodModel?
    fun create(food: FoodModel)
    fun update(food: FoodModel)
    fun delete(food: FoodModel)

    fun findAll(foodList: MutableLiveData<List<FoodModel>>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, food: FoodModel)

}