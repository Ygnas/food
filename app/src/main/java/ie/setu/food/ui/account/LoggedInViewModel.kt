package ie.setu.food.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ie.setu.food.firebase.FirebaseAuthentication

class LoggedInViewModel(app: Application) : AndroidViewModel(app) {

    private var firebaseAuth = FirebaseAuthentication(app)
    var liveFirebaseUser = firebaseAuth.liveFirebaseUser
    var logout = firebaseAuth.logout

    init {
        firebaseAuth.authObserver()
    }

    fun logOut() {
        firebaseAuth.logOut()
    }
}