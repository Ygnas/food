package ie.setu.food.ui.foodlist

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.datepicker.MaterialDatePicker
import ie.setu.food.R
import ie.setu.food.adapters.FoodAdapter
import ie.setu.food.adapters.FoodListener
import ie.setu.food.databinding.FragmentFoodListBinding
import ie.setu.food.models.FoodModel
import ie.setu.food.ui.account.LoggedInViewModel
import java.util.Date
import java.util.Locale


class FoodListFragment : Fragment(), FoodListener {

    private lateinit var binding: FragmentFoodListBinding
    private val viewModel: FoodListViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodListBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        viewModel.observableFoodList.observe(viewLifecycleOwner) { foods ->
            foods?.let {
                render(foods as ArrayList<FoodModel>)
            }
            binding.swiperefresh.isRefreshing = false
        }

        viewModel.observableMainFoodList.observe(viewLifecycleOwner) { foods ->
            foods?.let {
                viewModel.search("")
            }
        }

        binding.swiperefresh.setOnRefreshListener {
            binding.swiperefresh.isRefreshing = true
            viewModel.load()
        }
        setButtonListener()
        swipeTouchHelper.attachToRecyclerView(binding.recyclerView)
        return binding.root
    }

    private fun render(foodList: ArrayList<FoodModel>) {
        binding.recyclerView.adapter = FoodAdapter(foodList, this)
        if (foodList.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onFoodClick(food: FoodModel, position: Int) {
        findNavController().navigate(FoodListFragmentDirections.actionNavHomeToFoodFragment(food))
    }

    override fun onResume() {
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser != null) {
                viewModel.liveFirebaseUser.value = firebaseUser
                viewModel.load()
            }
        }
    }

    private fun setButtonListener() {
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.search(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText!!)
                return true
            }
        })

        binding.imageButton.setOnClickListener {
            binding.searchView.setQuery("", false)
            binding.searchView.isIconified = true
            filterDate()
        }

        binding.filterChip.setOnClickListener {
            binding.filterChip.visibility = View.GONE
            viewModel.filterByDate("")
        }

        binding.switch1.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            viewModel.filterFav(b)
        }


    }

    private fun filterDate() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Filter By Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener { date ->
            val selectedDate = Date(date)
            val formattedDate = SimpleDateFormat(
                "dd MMM yyyy",
                Locale.UK
            ).format(selectedDate)
            viewModel.filterByDate(formattedDate)
            setChip(formattedDate)
        }
        datePicker.show(childFragmentManager, "")
    }

    private fun setChip(text: String) {
        binding.filterChip.text = getString(R.string.filtering_by_date, text)
        binding.filterChip.visibility = View.VISIBLE
    }

    private var swipeTouchHelper = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder, target: ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    val uid = (viewHolder.itemView.tag as FoodModel).uid.toString()
                    (binding.recyclerView.adapter as FoodAdapter).removeAt(viewHolder.adapterPosition)
                    viewModel.deleteImage(uid)
                    viewModel.delete(
                        viewModel.liveFirebaseUser.value?.uid.toString(),
                        uid
                    )
                }
                if (direction == ItemTouchHelper.RIGHT) {
                    val food = (viewHolder.itemView.tag as FoodModel)
                    binding.switch1.isChecked = false
                    viewModel.setFav(food)
                    viewHolder.itemView.translationX = 0f
                    (binding.recyclerView.adapter as FoodAdapter).notifyItemChanged(viewHolder.adapterPosition)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val startRed = viewHolder.itemView.right.toFloat() + dX
                    val startGreen = viewHolder.itemView.left.toFloat() + dX
                    val deleteIcon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.baseline_delete_24)
                    val facIcon =
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.baseline_auto_awesome_24
                        )
                    val paint = Paint()
                    if (dX < 0) {

                        paint.color = Color.RED
                        c.drawRect(
                            startRed,
                            viewHolder.itemView.top.toFloat(),
                            viewHolder.itemView.right.toFloat(),
                            viewHolder.itemView.bottom.toFloat(),
                            paint
                        )

                        val iconMargin =
                            (viewHolder.itemView.height - deleteIcon?.intrinsicHeight!!) / 2
                        val iconTop =
                            viewHolder.itemView.top + (viewHolder.itemView.height - deleteIcon.intrinsicHeight) / 2
                        val iconLeft =
                            viewHolder.itemView.right - iconMargin - deleteIcon.intrinsicWidth
                        val iconRight = viewHolder.itemView.right - iconMargin
                        val iconBottom = iconTop + deleteIcon.intrinsicHeight

                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        deleteIcon.draw(c)
                    } else {
                        val food = viewHolder.itemView.tag as FoodModel
                        paint.color = if (food.fav) Color.RED else Color.GREEN
                        c.drawRect(
                            viewHolder.itemView.left.toFloat(),
                            viewHolder.itemView.top.toFloat(),
                            startGreen,
                            viewHolder.itemView.bottom.toFloat(),
                            paint
                        )

                        val favIconTop =
                            viewHolder.itemView.top + (viewHolder.itemView.height - facIcon?.intrinsicHeight!!) / 2
                        val favIconMargin =
                            (viewHolder.itemView.height - facIcon.intrinsicHeight) / 2
                        val favIconLeft = viewHolder.itemView.left + favIconMargin
                        val favIconRight =
                            viewHolder.itemView.left + favIconMargin + facIcon.intrinsicWidth
                        val favIconBottom = favIconTop + facIcon.intrinsicHeight

                        // Draw the edit icon
                        facIcon.setBounds(favIconLeft, favIconTop, favIconRight, favIconBottom)
                        facIcon.draw(c)
                    }
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
        })
}