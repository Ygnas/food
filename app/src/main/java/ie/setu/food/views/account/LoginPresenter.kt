package ie.setu.food.views.account

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.setu.food.main.MainApp
import ie.setu.food.models.UserModel
import ie.setu.food.views.foodlist.FoodListView

class LoginPresenter(val view: LoginView) {

    var app: MainApp = view.application as MainApp
    private lateinit var loginIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var registerIntentLauncher: ActivityResultLauncher<Intent>

    init {
        registerLoginCallback()
        registerRegisterCallback()
    }

    private fun getUsers() = app.users.findAll()


    fun login(username: String, password: String): UserModel? {
        val userList = getUsers()
        return userList.find { it.username == username && it.password == password }
    }

    fun doLogin() {
        val launcherIntent = Intent(view, FoodListView::class.java)
        loginIntentLauncher.launch(launcherIntent)
    }

    fun doRegister() {
        val launcherIntent = Intent(view, RegisterView::class.java)
        registerIntentLauncher.launch(launcherIntent)
    }

    private fun registerLoginCallback() {
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }

    private fun registerRegisterCallback() {
        registerIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }
}