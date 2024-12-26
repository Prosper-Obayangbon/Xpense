import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


import android.view.LayoutInflater
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import uk.ac.tees.mad.d3424757.xpenseapp.R


/**
 * A composable function that renders a pie chart (donut chart) using the MPAndroidChart library.
 *
 * This composable takes a map of data (categories and their associated values) and a color mapping
 * for each category. It displays the pie chart with each slice representing a category, with custom colors
 * and a "Total" label at the center of the donut chart.
 *
 * @param data A map where the key is the category name (String) and the value is the corresponding percentage (Float).
 * @param colorMapping A map where the key is the category name (String) and the value is the color assigned to the slice.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PieChartView(data: Map<String, Float>, colorMapping: Map<String, androidx.compose.ui.graphics.Color>) {

    val pieColors = data.keys.map { category ->
        colorMapping[category]?.toArgb() ?: Color.GRAY
    }

    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                description.isEnabled = false
                isDrawHoleEnabled = true
                setHoleColor(Color.TRANSPARENT)
                holeRadius = 80f
                transparentCircleRadius = 65f
                animateY(1400)
                setDrawEntryLabels(false)
                legend.isEnabled = true
            }
        },
        update = { pieChart ->
            val entries = data.map { (category, value) -> PieEntry(value, category) }
            val dataSet = PieDataSet(entries, "").apply {
                colors = pieColors
                setDrawValues(false)
            }
            val pieData = PieData(dataSet)
            pieChart.data = pieData

            val total = data.values.sum()
            val totalText = SpannableString("%.0f".format(total)).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
            }

            pieChart.centerText = "£ $totalText"
            pieChart.setCenterTextSize(30f)
            pieChart.setCenterTextColor(Color.BLACK)

            pieChart.invalidate()
        }
    )
}

/**
 * A composable function that renders a line chart using the MPAndroidChart library.
 * The chart displays a series of data points (entries) with a customizable label.
 * It also calculates and displays the total sum of the y-values from the entries.
 *
 * @param entries A list of `Entry` objects representing the data points in the line chart.
 * @param label The label for the data set in the line chart.
 * @param context The context to access resources for styling and the chart's background.
 */
@Composable
fun LineChart(entries: List<Entry>, label: String, context: Context) {
    val totalAmount = entries.sumOf { it.y.toDouble() }

    AndroidView(
        factory = {
            val view = LayoutInflater.from(context).inflate(R.layout.charts, null)
            view
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) { view ->
        val lineChart = view.findViewById<LineChart>(R.id.lineChart)

        val dataSet = LineDataSet(entries, label).apply {
            color = android.graphics.Color.parseColor("#FF2F7E79")
            valueTextColor = android.graphics.Color.BLACK
            lineWidth = 3f
            axisDependency = YAxis.AxisDependency.RIGHT
            setDrawFilled(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextSize = 12f
            valueTextColor = android.graphics.Color.parseColor("#FF2F7E79")
            val drawable = ContextCompat.getDrawable(context, R.drawable.chart_gredient_background)
            drawable?.let { fillDrawable = it }
        }

        lineChart.xAxis.isEnabled = false

        lineChart.data = LineData(dataSet)
        lineChart.axisLeft.isEnabled = false
        lineChart.axisRight.isEnabled = false
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.xAxis.setDrawAxisLine(false)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.invalidate()

        val totalAmountTextView = view.findViewById<TextView>(R.id.totalAmountText)
        totalAmountTextView.text = "£ ${"%.0f".format(totalAmount)}"
        totalAmountTextView.setTypeface(null, Typeface.BOLD)
        totalAmountTextView.textSize = 30f
    }
}
