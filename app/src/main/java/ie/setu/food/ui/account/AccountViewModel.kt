package ie.setu.food.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ie.setu.food.firebase.FirebaseAuthentication

class AccountViewModel(app: Application) : AndroidViewModel(app) {
    private var firebaseAuth = FirebaseAuthentication(app)
    var liveFirebaseUser = firebaseAuth.liveFirebaseUser
    var errorStatus = firebaseAuth.errorStatus


    fun login(email: String?, password: String?) {
        firebaseAuth.login(email, password)
    }

    fun register(email: String, password: String) {
        firebaseAuth.register(email, password)
    }
}