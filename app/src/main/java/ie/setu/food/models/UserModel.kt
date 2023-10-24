package ie.setu.food.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var id: Long = 0,
    var username: String = "",
    var password: String = "",
    var image: Uri = Uri.EMPTY) : Parcelable