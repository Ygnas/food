package ie.setu.food.views.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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

        setSupportActionBar(this.binding.toolbaraccount).apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
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
            if (password.length < 5) {
                binding.loginError.text = getString(R.string.register_error_short)
                return@setOnClickListener
            }
            if (password != passwordrepeat) {
                binding.loginError.text = getString(R.string.register_error_password)
                return@setOnClickListener
            }
            val user = presenter.register(
                username,
                password)

            if (user != null) {
                presenter.doLogin()
            } else {
                binding.loginError.text = getString(R.string.register_error)
            }
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