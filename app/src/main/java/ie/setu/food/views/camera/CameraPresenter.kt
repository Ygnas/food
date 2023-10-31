package ie.setu.food.views.camera

import android.content.ContentValues
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import ie.setu.food.main.MainApp
import timber.log.Timber
import java.util.Locale

class CameraPresenter(val view: CameraView) {

    var app: MainApp = view.application as MainApp
    var imageCapture: ImageCapture? = null

    fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val name = SimpleDateFormat(CameraView.FILENAME_FORMAT, Locale.UK)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Food")
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(view.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(view),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Timber.i("Photo capture failed")
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(app.baseContext, msg, Toast.LENGTH_SHORT).show()
                    Timber.i(msg)
                }
            }
        )
    }

    fun requestPermissions() {
        view.activityResultLauncher.launch(CameraView.REQUIRED_PERMISSIONS)
    }

    fun allPermissionsGranted() = CameraView.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            app.baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}