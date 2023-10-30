package ie.setu.food.views.account

import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import ie.setu.food.main.MainApp
import ie.setu.food.models.UserModel
import ie.setu.food.views.foodlist.FoodListView

class AccountPresenter(val view: AccountView) {

    var app: MainApp = view.application as MainApp
    private lateinit var logoutIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mainIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var sharedPreferences: SharedPreferences

    init {
        shared()
        registerLoginCallback()
        registerMainCallback()
    }

    private fun registerLoginCallback() {
        logoutIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }

    private fun registerMainCallback() {
        mainIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }

    fun checkAccount(): UserModel? {
        try {
            val id = sharedPreferences.getString("user", "")!!.toLong()
            val user = app.users.findById(id)
            if (user != null) {
                return user
            }
        } catch (e: Exception) {
            return null
        }
        return null
    }

    fun doLogout() {
        val editor = sharedPreferences.edit()
        editor.remove("user")
        editor.apply()
        doShowLogout()
    }

    fun doShowLogout() {
        val launcherIntent = Intent(view, LoginView::class.java)
        view.finish()
        logoutIntentLauncher.launch(launcherIntent)
    }

    fun doShowMain() {
        val launcherIntent = Intent(view, FoodListView::class.java)
        mainIntentLauncher.launch(launcherIntent)
    }

    private fun shared() {
        val masterKey: MasterKey = MasterKey.Builder(app.applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            app.applicationContext,
            "loggedUser",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}