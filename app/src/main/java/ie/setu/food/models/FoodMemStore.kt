package ie.setu.food.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class FoodMemStore: FoodStore {

    val placemarks = ArrayList<FoodModel>()

    override fun findAll(): List<FoodModel> {
        return placemarks
    }

    override fun findById(id: Long): FoodModel? {
        TODO("Not yet implemented")
    }

    override fun create(placemark: FoodModel) {
        placemark.id = getId()
        placemarks.add(placemark)
        logAll()
    }

    override fun update(food: FoodModel) {
        TODO("Not yet implemented")
    }

    override fun delete(placemark: FoodModel) {
        placemarks.remove(placemark)
    }

    fun logAll() {
        placemarks.forEach{ i("$it") }
    }
}