package ie.setu.food.views.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import ie.setu.food.databinding.ActivityAccountViewBinding
import ie.setu.food.firebase.FirebaseAuthentication
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

        val auth  = FirebaseAuthentication(app)
        val liveFirebaseUser : MutableLiveData<FirebaseUser?> = auth.liveFirebaseUser

        binding.toolbaraccount.title = title
        liveFirebaseUser.observe(this) { user ->
            binding.userUsername.text = user?.email
        }

        binding.buttonLogout.setOnClickListener{
            auth.logOut()
            presenter.doShowLogout()

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