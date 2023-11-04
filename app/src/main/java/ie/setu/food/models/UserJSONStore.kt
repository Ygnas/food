package ie.setu.food.models

import android.content.Context
import com.google.gson.reflect.TypeToken
import ie.setu.food.helpers.exists
import ie.setu.food.helpers.read
import ie.setu.food.helpers.write
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val USER_FILE = "user.json"
val userType: Type = object : TypeToken<ArrayList<UserModel>>() {}.type

class UserJSONStore(private val context: Context) : UserStore {

    private var users = mutableListOf<UserModel>()
    init {
        if (exists(context, USER_FILE)) {
            deserialize()
        }
    }
    override fun findAll(): MutableList<UserModel> {
        logAll()
        return users
    }
    override fun findById(id: Long): UserModel? {
        return users.find { it.id == id }
    }
    override fun create(user: UserModel) {
        user.id = generateRandomId()
        users.add(user)
        serialize()
    }

    override fun update(user: UserModel) {
        TODO("Not yet implemented")
    }

    override fun delete(user: UserModel) {
        TODO("Not yet implemented")
    }

    override fun userToJSON(user: UserModel): String {
        return gsonBuilder.toJson(user, userType)
    }

    override fun JSONToUser(user: String): UserModel {
        return gsonBuilder.fromJson(user, userType)
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(users, userType)
        write(context, USER_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, USER_FILE)
        users = gsonBuilder.fromJson(jsonString, userType)
    }

    private fun logAll() {
        users.forEach { Timber.i("$it") }
    }
}
