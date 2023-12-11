package ie.setu.food.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class FoodMemStore: FoodStore {

    val foods = ArrayList<FoodModel>()

    override fun findAll(): List<FoodModel> {
        return foods
    }

    override fun findAll(foodList: MutableLiveData<List<FoodModel>>) {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): FoodModel? {
        val foundFood: FoodModel? = foods.find { it.id == id }
        return foundFood
    }

    override fun findById(id: String, foods: List<FoodModel>): FoodModel? {
        TODO("Not yet implemented")
    }

    fun create(food: List<FoodModel>) {
        TODO("Not yet implemented")
    }

    fun create(food: MutableLiveData<List<FoodModel>>) {
        TODO("Not yet implemented")
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, food: FoodModel) {
        TODO("Not yet implemented")
    }

    override fun create(food: FoodModel) {
        food.id = getId()
        foods.add(food)
        logAll()
    }

    override fun update(food: FoodModel) {
        val foundFood: FoodModel? = foods.find { p -> p.id == food.id }
        if (foundFood != null) {
            foundFood.title = food.title
            foundFood.description = food.description
            foundFood.image = food.image
            foundFood.date = food.date
            foundFood.lat = food.lat
            foundFood.lng = food.lng
            foundFood.zoom = food.zoom
            foundFood.foodType = food.foodType
            logAll()
        }
    }

    override fun delete(food: FoodModel) {
        foods.remove(food)
    }

    fun logAll() {
        foods.forEach { i("$it") }
    }
}
