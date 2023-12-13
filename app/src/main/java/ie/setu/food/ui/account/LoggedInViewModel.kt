package ie.setu.food.ui.account

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.firebase.ui.auth.AuthUI
import ie.setu.food.firebase.FirebaseAuthentication

class LoggedInViewModel(app: Application) : AndroidViewModel(app) {

    private var firebaseAuth = FirebaseAuthentication(app)
    var liveFirebaseUser = firebaseAuth.liveFirebaseUser
    var logout = firebaseAuth.logout

    init {
        firebaseAuth.authObserver()
    }

    fun logOut(context: Context) {
        firebaseAuth.logOut()
        try {
            AuthUI.getInstance().signOut(context)
        } catch (_: Exception) {
        }
    }
}