package ie.setu.food.views.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.util.Patterns
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import ie.setu.food.R
import ie.setu.food.databinding.ActivityRegisterViewBinding
import ie.setu.food.firebase.FirebaseAuthentication
import ie.setu.food.main.MainApp
import java.util.regex.Pattern

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

        setSupportActionBar(this.binding.toolbaraccount).apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        val auth  = FirebaseAuthentication(app)
        val liveFirebaseUser : MutableLiveData<FirebaseUser?> = auth.liveFirebaseUser
        val authError : MutableLiveData<Boolean> = auth.errorStatus

        liveFirebaseUser.observe(this) { user ->
            presenter.doLogin()
        }

        authError.observe(this) { error ->
            if (error) {
                binding.loginError.text = getString(R.string.register_error)
            }
        }

        binding.toolbaraccount.title = title

        binding.buttonRegisterAccount.setOnClickListener{
            val username = binding.editTextUser.text.toString().lowercase()
            val password = binding.editTextPass.text.toString()
            val passwordrepeat = binding.editTextRepeatPass.text.toString()
            if (username.isEmpty() && password.isEmpty()) {
                binding.loginError.text = getString(R.string.register_error_empty)
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                binding.loginError.text = getString(R.string.register_error_invalid_email)
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.loginError.text = getString(R.string.register_error_short)
                return@setOnClickListener
            }
            if (password != passwordrepeat) {
                binding.loginError.text = getString(R.string.register_error_password)
                return@setOnClickListener
            }
            auth.register(username, password)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                presenter.doLogin()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}