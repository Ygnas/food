package ie.setu.food.ui.food

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.food.firebase.FirebaseDB
import ie.setu.food.firebase.FirebaseStorage
import ie.setu.food.models.FoodModel

class FoodViewModel : ViewModel() {
    fun addFood(
        firebaseUser: MutableLiveData<FirebaseUser>,
        food: FoodModel
    ) {
        FirebaseDB.create(firebaseUser, food)
    }

    fun uploadImage(uid: String, bitmap: Bitmap) {
        FirebaseStorage.uploadImageToFirebase(uid, bitmap, true)
    }

    fun findById(uid: String, foods: List<FoodModel>): FoodModel? {
        return FirebaseDB.findById(uid,foods)
    }
}