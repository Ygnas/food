package ie.setu.food.models

import timber.log.Timber.i

class FoodMemStore: FoodStore {
    val placemarks = ArrayList<FoodModel>()

    override fun findAll(): List<FoodModel> {
        return placemarks
    }
    override fun create(placemark: FoodModel) {
        placemark.id = getId()
        placemarks.add(placemark)
        logAll()
    }
    override fun delete(placemark: FoodModel) {
        placemarks.remove(placemark)
    }

    fun logAll() {
        placemarks.forEach{ i("$it") }
    }
}