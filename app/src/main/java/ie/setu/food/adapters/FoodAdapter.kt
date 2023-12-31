package ie.setu.food.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.food.databinding.CardFoodBinding
import ie.setu.food.firebase.FirebaseStorage
import ie.setu.food.models.FoodModel


interface FoodListener {
    fun onFoodClick(food: FoodModel, position: Int)
}

class FoodAdapter constructor(
    private var foods: MutableList<FoodModel>,
    private val listener: FoodListener
) :
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

    fun removeAt(position: Int) {
        foods.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = foods.size

    class MainHolder(private val binding: CardFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: FoodModel, listener: FoodListener) {
            binding.root.tag = food
            binding.food = food
            FirebaseStorage.loadImageFromFirebase(food.uid!!, binding.imageIcon, 200)
            binding.root.setOnClickListener { listener.onFoodClick(food, adapterPosition) }
            binding.executePendingBindings()
        }
    }
}
