package ie.setu.food.views.foodlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.food.databinding.CardFoodBinding
import ie.setu.food.models.FoodModel
import java.util.Date


interface FoodListener {
    fun onFoodClick(food: FoodModel, position : Int)
}

class FoodAdapter constructor(private var foods: List<FoodModel>, private val listener: FoodListener) :
    RecyclerView.Adapter<FoodAdapter.MainHolder>() {
    private var originalFoods: List<FoodModel> = foods
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardFoodBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val food = foods[holder.adapterPosition]
        holder.bind(food, listener)
    }

    override fun getItemCount(): Int = foods.size

    fun search(query: String) {
        foods = if (query.isEmpty()) {
            originalFoods
        } else {
            originalFoods.filter {
                it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true) || it.foodType.toString().contains(query, ignoreCase = true)
            }
        }
    }

    fun filterByDate(date: String) {
        foods = originalFoods.filter { it.date == date }
    }

    class MainHolder(private val binding: CardFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: FoodModel, listener: FoodListener) {
            binding.foodTitle.text = food.title
            binding.description.text = food.description
            Picasso.get().load(food.image).resize(200, 200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onFoodClick(food, adapterPosition) }
        }
    }
}
