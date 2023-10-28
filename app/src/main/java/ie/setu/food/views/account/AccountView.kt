package ie.setu.food.views.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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
        setSupportActionBar(this.binding.toolbaraccount).apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        binding.toolbaraccount.title = title

        val user = presenter.checkAccount()
        binding.userUsername.text = user!!.username

        binding.buttonLogout.setOnClickListener{
            presenter.doLogout()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                presenter.doShowLogout()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}