package ie.setu.food.models

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

    override fun findById(id: Long): FoodModel? {
        TODO("Not yet implemented")
    }

    override fun create(food: FoodModel) {
        food.id = getId()
        foods.add(food)
        logAll()
    }

    override fun update(food: FoodModel) {
        TODO("Not yet implemented")
    }

    override fun delete(food: FoodModel) {
        foods.remove(food)
    }

    fun logAll() {
        foods.forEach{ i("$it") }
    }
}