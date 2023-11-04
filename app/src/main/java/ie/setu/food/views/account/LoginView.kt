package ie.setu.food.views.account

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import ie.setu.food.R
import ie.setu.food.databinding.ActivityLoginViewBinding
import ie.setu.food.main.MainApp


class LoginView : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityLoginViewBinding
    private lateinit var presenter: LoginPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = LoginPresenter(this)

        app = application as MainApp

        val masterKey: MasterKey = MasterKey.Builder(applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            applicationContext,
            "loggedUser",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        try {
            val id = sharedPreferences.getString("user", "")!!.toLong()
            val user = app.users.findById(id)
            if (user != null) {
                presenter.doAccount()
            }
        } catch (e: Exception) {
            binding.loginError.text = ""
        }


        binding.buttonLogin.setOnClickListener{
            val user = presenter.login(binding.editTextUser.text.toString(), binding.editTextPass.text.toString())

            if (user != null) {
                binding.loginError.text = ""
                val editor = sharedPreferences.edit()
                editor.putString("user", user.id.toString())
                editor.apply()
                Toast.makeText(app.applicationContext, getString(R.string.success_login), Toast.LENGTH_LONG).show()
                presenter.doLogin()
            } else {
                binding.loginError.text = getString(R.string.incorrect_login)
            }
        }

        binding.buttonRegister.setOnClickListener{
            presenter.doRegister()
        }
    }
}