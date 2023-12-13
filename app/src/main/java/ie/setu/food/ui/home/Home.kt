package ie.setu.food.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import ie.setu.food.R
import ie.setu.food.databinding.DrawerHeaderBinding
import ie.setu.food.databinding.HomeMainBinding
import ie.setu.food.ui.account.LoggedInViewModel
import ie.setu.food.ui.foodlist.FoodListFragmentDirections

class Home : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var homeBinding: HomeMainBinding
    private lateinit var loggedInViewModel: LoggedInViewModel
    private lateinit var drawerHeaderBinding: DrawerHeaderBinding
    private lateinit var headerView: View
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = HomeMainBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        loggedInViewModel = ViewModelProvider(this)[LoggedInViewModel::class.java]
        headerView = homeBinding.navView.getHeaderView(0)
        drawerHeaderBinding = DrawerHeaderBinding.bind(headerView)
        setSupportActionBar(homeBinding.homeBar.toolbarHome)

        val drawerLayout: DrawerLayout = homeBinding.drawerLayout
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.home_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val navView: NavigationView = homeBinding.navView

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.galleryFragment
            ), drawerLayout
        )

        drawerHeaderBinding.logoutBtn.setOnClickListener {
            loggedInViewModel.logOut(applicationContext)
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        drawerHeaderBinding.logoutBtn.visibility = View.GONE
        loggedInViewModel.liveFirebaseUser.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                updateDrawerHeader(firebaseUser)
            }
        }

        loggedInViewModel.logout.observe(this) { logout ->
            if (logout) {
                homeBinding.drawerLayout.closeDrawers()
                drawerHeaderBinding.username.text = ""
                drawerHeaderBinding.logoutBtn.visibility = View.GONE
                homeBinding.navView.menu.findItem(R.id.loginFragment).isVisible = true
                navController.navigate(FoodListFragmentDirections.actionNavHomeToLoginFragment())
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.home_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun updateDrawerHeader(user: FirebaseUser) {
        drawerHeaderBinding.username.text = user.email
        drawerHeaderBinding.logoutBtn.visibility = View.VISIBLE
        homeBinding.navView.menu.findItem(R.id.loginFragment).isVisible = false
    }
}