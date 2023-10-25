package ie.setu.food.views.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
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


        binding.buttonLogin.setOnClickListener{
            val user = presenter.login(binding.editTextUser.text.toString(), binding.editTextPass.text.toString())

            if (user != null) {
                binding.loginError.text = ""
//                val userJson = app.users.userToJSON(user)
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