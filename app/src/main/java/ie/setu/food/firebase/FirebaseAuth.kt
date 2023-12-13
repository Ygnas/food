package ie.setu.food.firebase

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber.i

class FirebaseAuthentication(application: Application) {
    private var application: Application? = null

    private var firebaseAuth: FirebaseAuth
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var errorStatus = MutableLiveData<Boolean>()
    var logout = MutableLiveData<Boolean>()

    init {
        this.application = application
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            liveFirebaseUser.postValue(firebaseAuth.currentUser)
            errorStatus.postValue(false)
        }
    }

    fun authObserver() {
        firebaseAuth.addAuthStateListener { auth ->
            if (auth.currentUser != null) {
                liveFirebaseUser.postValue(auth.currentUser)
            }

        }
    }

    fun login(email: String?, password: String?) {
        firebaseAuth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    liveFirebaseUser.postValue(firebaseAuth.currentUser)
                    errorStatus.postValue(false)
                } else {
                    i("Login Failure: $task.exception!!.message")
                    errorStatus.postValue(true)
                }
            }
    }

    fun register(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    liveFirebaseUser.postValue(firebaseAuth.currentUser)
                    errorStatus.postValue(false)
                } else {
                    i("Registration Failure: $task.exception!!.message")
                    errorStatus.postValue(true)
                }
            }
    }

    fun logOut() {
        firebaseAuth.signOut()
        logout.postValue(true)
        errorStatus.postValue(false)
    }
}