package ie.setu.food.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.setu.food.helpers.exists
import ie.setu.food.helpers.read
import ie.setu.food.helpers.write
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "food.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<FoodModel>>() {}.type
fun generateRandomId(): Long {
    return Random().nextLong()
}
class FoodJSONStore(private val context: Context) : FoodStore {

    var foods = mutableListOf<FoodModel>()
    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }
    override fun findAll(): MutableList<FoodModel> {
        logAll()
        return foods
    }
    override fun findById(id: Long): FoodModel? {
        val foundFood: FoodModel? = foods.find { it.id == id }
        return foundFood
    }
    override fun create(food: FoodModel) {
        food.id = generateRandomId()
        foods.add(food)
        serialize()
    }

    override fun update(food: FoodModel) {
        val foodList = findAll() as ArrayList<FoodModel>
        var foundFood: FoodModel? = foodList.find { p -> p.id == food.id }
        if (foundFood != null) {
            foundFood.title = food.title
            foundFood.description = food.description
            foundFood.image = food.image
            foundFood.lat = food.lat
            foundFood.lng = food.lng
            foundFood.zoom = food.zoom
        }
        serialize()
    }

    override fun delete(food: FoodModel) {
        foods.remove(food)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(foods, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        foods = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        foods.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
