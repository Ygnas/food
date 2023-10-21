package ie.setu.food.views.foodlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.food.R
import ie.setu.food.databinding.ActivityFoodListBinding
import ie.setu.food.main.MainApp
import ie.setu.food.models.FoodModel

class FoodListView : AppCompatActivity(), FoodListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityFoodListBinding
    private lateinit var presenter: FoodListPresenter
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = FoodListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadFoods()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                presenter.doAddPlacemark()
            }

            R.id.item_map -> {
                presenter.doShowPlacemarksMap()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onFoodClick(food: FoodModel, position: Int) {
        this.position = position
        presenter.doEditPlacemark(food, this.position)
    }

    private fun loadFoods() {
        binding.recyclerView.adapter = FoodAdapter(presenter.getPlacemarks(), this)
        onRefresh()
    }

    fun onRefresh() {
        binding.recyclerView.adapter?.notifyItemRangeChanged(0, presenter.getPlacemarks().size)
    }

    fun onDelete(position: Int) {
        binding.recyclerView.adapter?.notifyItemRemoved(position)
    }
}
