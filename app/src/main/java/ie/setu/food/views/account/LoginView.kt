package ie.setu.food.views.account

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import ie.setu.food.R
import ie.setu.food.databinding.ActivityLoginViewBinding
import ie.setu.food.firebase.FirebaseAuthentication
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

        val auth  = FirebaseAuthentication(app)
        val liveFirebaseUser : MutableLiveData<FirebaseUser?> = auth.liveFirebaseUser
        val authError : MutableLiveData<Boolean> = auth.errorStatus

        setSupportActionBar(this.binding.toolbaraccount).apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        binding.toolbaraccount.title = title

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

        liveFirebaseUser.observe(this) { user ->
            presenter.doAccount()
        }

        authError.observe(this) { error ->
            if (error) {
              binding.loginError.text = getString(R.string.incorrect_login)
            }
        }

        binding.buttonLogin.setOnClickListener{
            auth.login(binding.editTextUser.text.toString().lowercase(), binding.editTextPass.text.toString())
        }

        binding.buttonRegister.setOnClickListener{
            presenter.doRegister()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                presenter.doShowMain()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}