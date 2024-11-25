import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.Utils
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LineChart(entries: List<Entry>, label: String, context: Context) {
    val totalAmount = entries.sumOf { it.y.toDouble() } // Calculate the total amount
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

        // Set chart data
        val dataSet = LineDataSet(entries, label).apply {
            color = android.graphics.Color.parseColor("#FF2F7E79")  // Use a specific color value
            valueTextColor = android.graphics.Color.BLACK
            lineWidth = 3f
            axisDependency = YAxis.AxisDependency.RIGHT
            setDrawFilled(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextSize = 12f
            valueTextColor = android.graphics.Color.parseColor("#FF2F7E79") // Match chart line color
            val drawable = ContextCompat.getDrawable(context, R.drawable.chart_gredient_background)
            drawable?.let { fillDrawable = it }
        }
        // Disable the X-axis completely
        lineChart.xAxis.isEnabled = false

        // Set chart data
        lineChart.data = LineData(dataSet)
        lineChart.axisLeft.isEnabled = false
        lineChart.axisRight.isEnabled = false
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.xAxis.setDrawAxisLine(false)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.invalidate()



        // Update the total amount text
        val totalAmountTextView = view.findViewById<TextView>(R.id.totalAmountText)
        totalAmountTextView.text = "£ ${"%.0f".format(totalAmount)}"

        // Make the TextView text bold
        totalAmountTextView.setTypeface(null, Typeface.BOLD)
        totalAmountTextView.textSize = 30f
    }
}



