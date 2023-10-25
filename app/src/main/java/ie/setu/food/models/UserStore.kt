package ie.setu.food.models

interface UserStore {
    fun findAll(): List<UserModel>
    fun findById(id:Long): UserModel?
    fun create(user: UserModel)
    fun update(user: UserModel)
    fun delete(user: UserModel)
    fun userToJSON(user: UserModel): String
    fun JSONToUser(user: String): UserModel
}