package ie.setu.food.views.food

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.food.R
import ie.setu.food.databinding.ActivityFoodBinding
import ie.setu.food.models.FoodModel
import timber.log.Timber

class FoodView : AppCompatActivity() {

    private lateinit var binding: ActivityFoodBinding
    private lateinit var presenter: FoodPresenter
    var food = FoodModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = FoodPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheFood(binding.foodTitle.text.toString(), binding.foodDescription.text.toString())
            presenter.doSelectImage()
        }

        binding.foodLocation.setOnClickListener {
            presenter.cacheFood(binding.foodTitle.text.toString(), binding.foodDescription.text.toString())
            presenter.doSetLocation()
        }

        binding.btnAdd.setOnClickListener {
            if (binding.foodTitle.text.toString().isEmpty()) {
                Snackbar.make(binding.root, R.string.enter_food_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                presenter.doAddOrSave(
                    binding.foodTitle.text.toString(),
                    binding.foodDescription.text.toString()
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_food, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        deleteMenu.isVisible = presenter.edit
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showFood(food: FoodModel) {
        binding.foodTitle.setText(food.title)
        binding.foodDescription.setText(food.description)
        binding.btnAdd.setText(R.string.save_food)
        Picasso.get()
            .load(food.image)
            .into(binding.foodImage)
        if (food.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_food_image)
        }
    }

    fun updateImage(image: Uri) {
        Timber.i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.foodImage)
        binding.chooseImage.setText(R.string.change_food_image)
    }
}

