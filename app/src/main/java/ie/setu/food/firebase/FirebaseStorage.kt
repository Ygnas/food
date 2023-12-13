package ie.setu.food.firebase

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

object FirebaseStorage {

    private var storage = FirebaseStorage.getInstance().reference
    private var imageUri = MutableLiveData<Uri>()

    fun uploadImageToFirebase(uid: String, bitmap: Bitmap, updating: Boolean) {
        val imageRef = storage.child("images").child("$uid.jpg")
        val stream = ByteArrayOutputStream()

        val targetSize = 200 * 1024
        var quality = 100

        do {
            stream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            quality -= 5
        } while (stream.toByteArray().size > targetSize && quality > 10)

        val data: ByteArray = stream.toByteArray()

        var uploadTask = imageRef.putBytes(data)

        uploadTask.addOnSuccessListener { task ->
            task.metadata?.reference?.downloadUrl?.addOnCompleteListener { downloadTask ->
                imageUri.value = downloadTask.result
                if (updating) {
                    FirebaseDB.updateImageRef(uid, imageUri.value.toString())
                }
            }
        }.addOnFailureListener {
            uploadTask = imageRef.putBytes(data)
            uploadTask.addOnSuccessListener { ut ->
                ut.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                    imageUri.value = task.result!!
                }
            }
        }
    }

    fun loadImageFromFirebase(uid: String, imageView: ImageView, size: Int) {
        val imageRef = storage.child("images").child("$uid.jpg")

        imageRef.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()

            Picasso.get().load(imageUrl).resize(size, size).into(imageView)
        }.addOnFailureListener {}
    }
}
