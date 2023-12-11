package ie.setu.food.views.foodlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import ie.setu.food.R
import ie.setu.food.adapters.FoodAdapter
import ie.setu.food.adapters.FoodListener
import ie.setu.food.databinding.ActivityFoodListBinding
import ie.setu.food.main.MainApp
import ie.setu.food.models.FoodModel

class FoodListView : AppCompatActivity(), FoodListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityFoodListBinding
    private lateinit var presenter: FoodListPresenter
    private lateinit var drawerLayout: DrawerLayout
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = FoodListPresenter(this)
        app = application as MainApp

        drawer()
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener { menuItem ->
            onNavigationItemSelected(menuItem)
        }
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadFoods()
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchFoods(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                searchFoods(newText!!)
                return true
            }

        })

        binding.imageButton.setOnClickListener {
            binding.searchView.setQuery("", false)
            binding.searchView.isIconified = true
            presenter.filterDate()
        }

        binding.filterChip.setOnClickListener {
            binding.filterChip.visibility = View.GONE
//            filterFoodsByDate("")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun drawer() {
        drawerLayout = findViewById(R.id.drawer_layout)
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.drawer_open,
            R.string.drawer_close
        )

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                presenter.doAddFood()
            }
            R.id.item_gallery -> {
                presenter.doShowGallery()
            }
            R.id.item_map -> {
                presenter.doShowFoodsMap()
            }
            android.R.id.home -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                presenter.doAddFood()
            }
            R.id.item_gallery -> {
                presenter.doShowGallery()
            }
            R.id.item_map -> {
                presenter.doShowFoodsMap()
            }
            R.id.foodFragment -> {
                presenter.doShowLogin()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START, false)
        return true
    }

    override fun onFoodClick(food: FoodModel, position: Int) {
        this.position = position
        presenter.doEditFood(food, this.position)
    }

    private fun loadFoods() {
        binding.recyclerView.adapter = FoodAdapter(presenter.getFoods(), this)
        onRefresh()
    }

    private fun searchFoods(query: String) {
        val foodAdapter = FoodAdapter(presenter.getFoods(),this)
//        foodAdapter.search(query)
        binding.recyclerView.adapter = foodAdapter
        onRefresh()
    }
//
//    fun filterFoodsByDate(query: String) {
//        val foodAdapter = FoodAdapter(presenter.getFoods(),this)
//        foodAdapter.filterByDate(query)
//        binding.recyclerView.adapter = foodAdapter
//        onRefresh()
//    }

    fun setChip(text: String) {
        binding.filterChip.text = "Filtering By Date: $text"
        binding.filterChip.visibility = View.VISIBLE
    }

    fun onRefresh() {
        binding.recyclerView.adapter?.notifyItemRangeChanged(0, presenter.getFoods().size)
    }

    fun onDelete(position: Int) {
        binding.recyclerView.adapter?.notifyItemRemoved(position)
    }
}
