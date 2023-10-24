package ie.setu.food.views.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.security.crypto.MasterKeys
import ie.setu.food.R
import ie.setu.food.databinding.ActivityRegisterViewBinding
import ie.setu.food.main.MainApp

class RegisterView : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityRegisterViewBinding
    private lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = RegisterPresenter(this)
        app = application as MainApp

        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "user",
            masterKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )


        binding.buttonRegisterAccount.setOnClickListener{
            val user = presenter.register(
                binding.editTextUser.text.toString(),
                binding.editTextPass.text.toString(),
                binding.editTextRepeatPass.text.toString())

            if (user != null) {
                presenter.doLogin()
            } else {
                binding.loginError.text = getString(R.string.register_error)
            }
        }
    }
}