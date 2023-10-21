package ie.setu.food.models

interface FoodStore {
    fun findAll(): List<FoodModel>
    fun findById(id:Long): FoodModel?
    fun create(food: FoodModel)
    fun update(food: FoodModel)
    fun delete(food: FoodModel)
}