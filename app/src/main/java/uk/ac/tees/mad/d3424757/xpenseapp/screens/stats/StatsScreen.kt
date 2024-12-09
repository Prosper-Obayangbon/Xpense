package uk.ac.tees.mad.d3424757.xpenseapp.screens.stats

import LineChart
import PieChartView
import XTopBar
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.components.XDropDownSelector
import uk.ac.tees.mad.d3424757.xpenseapp.components.RecentTransactions
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.monthFilter
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionCategories.getCategoriesForTransactionType
import uk.ac.tees.mad.d3424757.xpenseapp.utils.toMonthName
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.StatsViewModel

/**
 * A composable function that displays the Stats Screen showing financial report with various charts and transaction data.
 *
 * @param viewModel The ViewModel that provides transaction data and related functionalities.
 * @param modifier A modifier for custom styling of the composable.
 * @param navController A navigation controller to handle navigation actions.
 * @param context The context for use with Android components.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatsScreen(
    viewModel: StatsViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
    context: Context
) {
    // State for transaction data, selected filters, and chart type
    val transactions by viewModel.transactions.collectAsState(emptyList())
    val selectedMonth = remember { mutableStateOf("All") } // Default to "All"
    val selectedChart = remember { mutableStateOf("Donut") }
    val selectedCategory = remember { mutableStateOf("Expense") }
    var selectedType by remember { mutableStateOf("Transaction") }
    val isIncome = selectedCategory.value == "Income" // Determine if "Income" is selected
    val categoryColorMap = remember { mutableStateOf(mapOf<String, Color>()) }

    // Filter transactions based on the selected month
    val filteredTransactions = if (selectedMonth.value == "All") {
        transactions // Show all transactions if "All" is selected
    } else {
        transactions.filter { transaction ->
            transaction.date.toMonthName() == selectedMonth.value
        }
    }

    // Main screen layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = mintCream)
    ) {
        // Top bar with navigation and title
        XTopBar(modifier = modifier,
            text = "Financial Report",
            textColor = Color.Black,
            backClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Row for Month Dropdown and Chart Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dropdown for selecting month
            XDropDownSelector(
                selectedItem = selectedMonth.value,
                options = monthFilter,
                onOptionSelected = { selectedMonth.value = it }
            )
            // Toggle button for chart type (Donut or Line)
            ChartToggle(selectedChart = selectedChart.value) { selectedChart.value = it }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Chart Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(mintCream),
            contentAlignment = Alignment.Center
        ) {
            when (selectedChart.value) {
                "Donut" -> {
                    selectedType = "Category"

                    // Get categories and color map
                    val categories = getCategoriesForTransactionType(isIncome)
                    categoryColorMap.value = viewModel.generateCategoryColorMap(categories)

                    // Filter transactions by selected category type
                    val filteredData = viewModel.filterTransactionsByType(
                        filteredTransactions,
                        type = selectedCategory.value
                    )

                    // Map filtered data to Pie Entries for Donut chart
                    val pieData = viewModel.mapToPieEntries(filteredData, categoryColorMap.value)

                    // Render the Donut Chart
                    PieChartView(
                        data = pieData,
                        colorMapping = categoryColorMap.value
                    )
                }

                else -> {
                    selectedType = "Transaction"

                    // Filter transactions by selected category type for Line chart
                    val filteredData = viewModel.filterTransactionsByType(
                        filteredTransactions,
                        type = selectedCategory.value
                    )

                    // Map filtered data to Line Chart entries
                    val entries = viewModel.mapTransactionsToEntries(filteredData)

                    // Render the Line Chart
                    LineChart(
                        entries = entries,
                        label = "Expense",
                        context = context
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Toggle for Expense/Income selection
        ExpenseIncomeToggle(
            selected = selectedCategory,
            onSelectionChange = { newSelection -> selectedCategory.value = newSelection }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Display Transaction List or Category Progress based on selected type
        if (selectedType == "Transaction") {
            val transType = viewModel.filterTransactionsByType(
                transactions = filteredTransactions,
                type = selectedCategory.value
            )
            if (transType.isEmpty()) {
                Text(
                    text = "No Transaction data available",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            } else {
                RecentTransactions(
                    transactions = transType
                )
            }
        } else {
            val (categories, total) = viewModel.calculateCategoryProgress(
                type = selectedCategory.value.uppercase(),
                transactions = filteredTransactions
            )

            if (categories.isEmpty()) {
                Text(
                    "No Transaction data available",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            } else {
                CategoryProgress(
                    categories = categories,
                    total = total,
                    colorMapping = categoryColorMap.value,
                )
            }
        }
    }
}


/**
 * A composable function that displays a toggle to switch between Donut and Line charts.
 *
 * @param selectedChart The currently selected chart type.
 * @param onToggle A callback to handle chart type toggling.
 */
@Composable
fun ChartToggle(
    selectedChart: String,
    onToggle: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.5f),
        horizontalArrangement = Arrangement.Center
    ) {
        // Donut Chart Button
        OutlinedButton(
            onClick = { onToggle("Donut") },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (selectedChart == "Donut") tealGreen else Color.Transparent,
                contentColor = if (selectedChart == "Donut") Color.White else tealGreen
            ),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(bottomStart = 8.dp, topStart = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_pie_chart_24),
                contentDescription = "Donut chart",
                modifier = Modifier.size(40.dp)
            )
        }

        // Line Chart Button
        OutlinedButton(
            onClick = { onToggle("Line") },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (selectedChart == "Line") tealGreen else Color.Transparent,
                contentColor = if (selectedChart == "Line") Color.White else tealGreen
            ),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(bottomEnd = 8.dp, topEnd = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.timeline),
                contentDescription = "Line chart",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

/**
 * A composable function that displays the progress of categories (Income/Expense) with a progress bar.
 *
 * @param categories A list of pairs containing category names and their corresponding amounts.
 * @param total The total amount for comparison to show progress.
 * @param colorMapping A map of category names to their respective colors.
 */
@Composable
fun CategoryProgress(
    categories: List<Pair<String, Double>>,
    total: Double,
    colorMapping: Map<String, Color>,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Display progress for each category
        categories.forEach { (category, amount) ->
            val progress = (amount / total).coerceIn(0.0, 1.0).toFloat() // Normalize progress to 0-1 range

            // Category Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$category: £${"%.2f".format(amount)}",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                )
                Text(
                    text = "%.2f%%".format(progress * 100),
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = colorMapping[category] ?: Color.Black
                    )
                )
            }

            // Progress Bar
            LinearProgressIndicator(
                progress = {
                    progress  // Directly use normalized progress (0.0 to 1.0)
                },
                modifier = Modifier.
                    fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .heightIn(10.dp),
                color = colorMapping[category] ?: Color.Gray,
            )
        }
    }
}


/**
 * A composable function that displays a toggle to switch between "Expense" and "Income" categories.
 *
 * @param selected The current selected category (either "Expense" or "Income").
 * @param onSelectionChange A callback to handle changes in the selected category.
 */
@Composable
fun ExpenseIncomeToggle(
    selected: MutableState<String>,
    onSelectionChange: (String) -> Unit
) {
    // Row for toggling between "Expense" and "Income"
    Row(
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "Expense" Button
        OutlinedButton(
            onClick = { onSelectionChange("Expense") },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (selected.value == "Expense") tealGreen else Color.Transparent,
                contentColor = if (selected.value == "Expense") Color.White else tealGreen
            ),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(bottomStart = 8.dp, topStart = 8.dp)
        ) {
            Text(text = "Expense", style = TextStyle(fontWeight = FontWeight.Bold))
        }

        // "Income" Button
        OutlinedButton(
            onClick = { onSelectionChange("Income") },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (selected.value == "Income") tealGreen else Color.Transparent,
                contentColor = if (selected.value == "Income") Color.White else tealGreen
            ),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(bottomEnd = 8.dp, topEnd = 8.dp)
        ) {
            Text(text = "Income", style = TextStyle(fontWeight = FontWeight.Bold))
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun Preview() {
//    // Mock ViewModel to simulate data for the preview.
//    //val mockViewModel = StatsViewModel(LocalContext.current)
//    // Add mock transaction data here if needed.
//
//    val navController = rememberNavController()
//
//    XpenseAppTheme {
//        // Displaying the AddScreen with mock data and a theme.
//        //StatsScreen(mockViewModel, navController = navController, context = LocalContext.current)
//    }
//}
