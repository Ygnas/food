package ie.setu.food.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodModel(var id: Long = 0,
                          var title: String = "",
                          var description: String = "",
@Parcelize
