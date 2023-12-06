package ie.setu.food.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import ie.setu.food.R
import ie.setu.food.databinding.HomeMainBinding
import ie.setu.food.models.FoodModel
import ie.setu.food.ui.foodlist.FoodListFragmentDirections

class Home : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var homeBinding: HomeMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = HomeMainBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        setSupportActionBar(homeBinding.homeBar.toolbarHome)

        val drawerLayout: DrawerLayout = homeBinding.drawerLayout
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.home_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: NavigationView = homeBinding.navView

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.foodFragment, R.id.galleryFragment
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.nav_home)
                }
                R.id.foodFragment -> {
                    navController.navigate(FoodListFragmentDirections.actionNavHomeToFoodFragment(FoodModel()))
                }
                R.id.galleryFragment -> {
                    navController.navigate(FoodListFragmentDirections.actionNavHomeToGalleryFragment())
                }
            }
            drawerLayout.closeDrawers()

            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.home_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}