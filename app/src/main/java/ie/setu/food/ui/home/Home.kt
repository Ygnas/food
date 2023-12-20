package ie.setu.food.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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

        drawerHeaderBinding.switch2.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            setDayNightMode(!b)
        }


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        checkNightMode()
    }

    private fun setDayNightMode(day: Boolean) {
        if (day) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) else AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )
    }

    private fun checkNightMode() {
        val nightMode = getString(R.string.test_night_mode)
        drawerHeaderBinding.switch2.isChecked = nightMode.toBoolean()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.foodFragment -> {
                navController.navigateUp()
                navController.navigate(FoodListFragmentDirections.actionNavHomeToFoodFragment())
                true
            }

            R.id.galleryFragment -> {
                navController.navigateUp()
                navController.navigate(FoodListFragmentDirections.actionNavHomeToGalleryFragment())
                true
            }

            R.id.foodMapFragment -> {
                navController.navigateUp()
                navController.navigate(FoodListFragmentDirections.actionNavHomeToFoodMapFragment())
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}