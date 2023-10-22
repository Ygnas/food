package ie.setu.food.views.gallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.food.databinding.CardFoodBinding
import ie.setu.food.databinding.CardImageBinding
import ie.setu.food.models.FoodModel


interface ImageListener {
    fun onFoodClick(food: FoodModel, position : Int)
}

class ImageAdapter constructor(private var foods: List<FoodModel>, private val listener: ImageListener) :
    RecyclerView.Adapter<ImageAdapter.MainHolder>() {

    private var filteredFoods: List<FoodModel> = foods.filter { it.image != Uri.EMPTY }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardImageBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val food = filteredFoods[holder.adapterPosition]
        holder.bind(food, listener)
    }

    override fun getItemCount(): Int = filteredFoods.size

    class MainHolder(private val binding: CardImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: FoodModel, listener: ImageListener) {
            Picasso.get().load(food.image).resize(450, 450).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onFoodClick(food, adapterPosition) }
        }
    }
}
