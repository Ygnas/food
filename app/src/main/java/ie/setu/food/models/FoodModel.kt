package ie.setu.food.models

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class FoodModel(
    var uid: String? = "",
    var title: String = "",
    var description: String = "",
    var image: Uri = Uri.EMPTY,
    var date: String = "",
    var foodType: FoodType = FoodType.BREAKFAST,
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f,
    var fav: Boolean = false
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "description" to description,
            "image" to image.toString(),
            "date" to date,
            "foodType" to foodType.name,
            "lat" to lat,
            "lng" to lng,
            "zoom" to zoom,
            "fav" to fav
        )
    }

    companion object {
        fun fromMap(snapshot: DataSnapshot): FoodModel {
            val value = snapshot.value as Map<String, Any?>
            return FoodModel(
                uid = value["uid"] as? String,
                title = value["title"] as String,
                description = value["description"] as String,
                image = value["image"]?.toString().let { Uri.parse(it) },
                date = value["date"] as String,
                foodType = (value["foodType"] as? String).let { FoodType.valueOf(it!!) },
                lat = (value["lat"] as Number).toDouble(),
                lng = (value["lng"] as Number).toDouble(),
                zoom = (value["zoom"] as Number).toFloat(),
                fav = value["fav"] as Boolean
            )
        }
    }
}

@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable

enum class FoodType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
}
