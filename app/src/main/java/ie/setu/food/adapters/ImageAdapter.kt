package ie.setu.food.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.food.databinding.CardImageBinding
import ie.setu.food.firebase.FirebaseStorage
import ie.setu.food.models.FoodModel


interface ImageListener {
    fun onFoodClick(food: FoodModel, position: Int)
}

class ImageAdapter constructor(foods: List<FoodModel>, private val listener: ImageListener) :
    RecyclerView.Adapter<ImageAdapter.MainHolder>() {

    //    private var filteredFoods: List<FoodModel> = foods.filter { it.image != Uri.EMPTY }
    private var filteredFoods: List<FoodModel> = foods

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
            FirebaseStorage.loadImageFromFirebase(food.uid!!, binding.imageIcon, 450)
            binding.root.setOnClickListener { listener.onFoodClick(food, adapterPosition) }
        }
    }
}
