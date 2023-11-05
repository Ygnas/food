package ie.setu.food.views.account

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.setu.food.main.MainApp
import ie.setu.food.models.UserModel

class RegisterPresenter(val view: RegisterView) {

    var app: MainApp = view.application as MainApp
    private lateinit var loginIntentLauncher: ActivityResultLauncher<Intent>

    init {
        registerLoginCallback()
    }

    fun register(username: String, password: String): UserModel? {
        val userList = app.users.findAll()
        val user = userList.find { it.username == username }

        return if (user == null) {
            val newUser = UserModel(
                username = username,
                password = password
            )
            app.users.create(newUser)
            newUser
        } else {
            null
        }
    }

    fun doLogin() {
        val launcherIntent = Intent(view, LoginView::class.java)
        loginIntentLauncher.launch(launcherIntent)
    }

    private fun registerLoginCallback() {
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }
}