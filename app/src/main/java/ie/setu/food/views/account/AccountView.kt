package ie.setu.food.views.account

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import ie.setu.food.R
import ie.setu.food.databinding.ActivityAccountViewBinding
import ie.setu.food.main.MainApp

class AccountView : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityAccountViewBinding
    private lateinit var presenter: AccountPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = AccountPresenter(this)

        app = application as MainApp

        binding.toolbaraccount.title = title

        val user = presenter.checkAccount()
        binding.userUsername.text = user!!.username

        binding.buttonLogout.setOnClickListener{
            presenter.doLogout()
        }
    }
}