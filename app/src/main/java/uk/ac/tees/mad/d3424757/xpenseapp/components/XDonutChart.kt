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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PieChartView(data: Map<String, Float>, colorMapping: Map<String, androidx.compose.ui.graphics.Color>) {

    val pieColors = data.keys.map { category ->
        colorMapping[category]?.toArgb() ?: Color.GRAY // Default to gray if not found
    }
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                description.isEnabled = false // Disable the chart description
                isDrawHoleEnabled = true      // Enable the "donut" effect
                setHoleColor(Color.TRANSPARENT) // Make the hole transparent
                holeRadius = 80f              // Adjust the size of the hole
                transparentCircleRadius = 65f // Add a transparent ring around the hole
                animateY(1400)                // Animate chart on the Y-axis
                setDrawEntryLabels(false)     // Disable labels on the slices
                legend.isEnabled = true       // Enable legend for categories
            }
        },
        update = { pieChart ->
            val entries = data.map { (category, value) -> PieEntry(value, category) }
            val dataSet = PieDataSet(entries, "").apply {
                colors = pieColors // Set slice colors
                setDrawValues(false) // Disable values on the chart
            }
            val pieData = PieData(dataSet)
            pieChart.data = pieData

            // Create a bold "Total" text using SpannableString
            val total = data.values.sum()
            val totalText = SpannableString("%.0f".format(total)).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, length, 0) // Make the "total" value bold
            }

            // Add total amount in the center of the donut
            pieChart.centerText = "£ $totalText"
            pieChart.setCenterTextSize(30f)
            pieChart.setCenterTextColor(Color.BLACK)

            pieChart.invalidate() // Refresh chart
        }
    )
}
