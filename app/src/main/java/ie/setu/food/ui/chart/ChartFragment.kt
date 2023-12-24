package ie.setu.food.ui.chart

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import ie.setu.food.databinding.FragmentChartBinding
import ie.setu.food.models.FoodType
import ie.setu.food.ui.foodlist.FoodListViewModel

class ChartFragment : Fragment() {

    private val viewModel: FoodListViewModel by activityViewModels()
    private lateinit var binding: FragmentChartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentChartBinding.inflate(layoutInflater)

        val totalEntries = mutableListOf<BarEntry>()
        val snackEntries = mutableListOf<BarEntry>()
        val chart = binding.lineChart

        viewModel.observableMainFoodList.observe(viewLifecycleOwner) { foods ->
            foods?.let {

                val groupedByDate = foods.sortedBy { it.date }.groupBy { it.date }
                val xAxisLabels = mutableListOf<String>()

                var i = 0f
                for ((date, foodList) in groupedByDate) {
                    xAxisLabels.add(date)
                    snackEntries.add(
                        BarEntry(
                            i,
                            foodList.count { it.foodType == FoodType.SNACK }.toFloat()
                        )
                    )
                    totalEntries.add(BarEntry(i, foodList.size.toFloat()))
                    i++
                }

                val totalSet = BarDataSet(totalEntries, "Total")
                val snackSet = BarDataSet(snackEntries, "Snacks")
                snackSet.color = Color.RED
                totalSet.color= Color.GREEN
                totalSet.valueTextSize = 14f
                snackSet.valueTextSize = 14f
                chart.legend.textSize = 14f
                chart.xAxis.granularity = 1f
                chart.axisLeft.granularity = 1f
                chart.axisRight.granularity = 1f
                chart.xAxis.setLabelCount(3, false)
                chart.xAxis.textSize = 14f
                chart.xAxis.setDrawGridLines(false)
                chart.axisLeft.setDrawGridLines(true)
                chart.axisRight.setDrawGridLines(true)


                val barData = BarData(totalSet, snackSet)
                chart.xAxis.setDrawLabels(true)
                chart.description.text = ""
                chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                chart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
                chart.data = barData
            }
        }

        return binding.root

    }
}