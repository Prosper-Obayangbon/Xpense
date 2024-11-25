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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.components.RecentTransactions
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionCategories.getCategoriesForTransactionType
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.StatsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatsScreen(
    viewModel: StatsViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
    context: Context
) {
    val transactions by viewModel.transactions.collectAsState(emptyList()) // Flow or LiveData
    val selectedChart = remember { mutableStateOf("Donut") }
    val selectedCategory = remember { mutableStateOf("Expense") }
    var selectedType by remember { mutableStateOf("Transaction") }
    val isIncome = selectedCategory.value == "Income"  // This determines whether it's Income or Expense
    val categoryColorMap = remember { mutableStateOf(mapOf<String, Color>()) }

    // Layout for the Stats screen
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = mintCream)
    ) {
        XTopBar(
            text = "Financial Report",
            textColor = Color.Black,
            backClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Row for Month Dropdown and Chart Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DropDownSelector(
                selectedItem = "Month",
                options = listOf("January", "February", "March", "April", "May", "June", "July"),
                onOptionSelected = { /* TODO */ }
            )
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

                    // Filter transactions by selected category type (Income/Expense)
                    val filteredData = viewModel.filterTransactionsByType(
                        transactions,
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
                    // For Line Chart, set the type to "Transaction"
                    selectedType = "Transaction"

                    // Filter transactions by selected category type (Income/Expense)
                    val filteredData = viewModel.filterTransactionsByType(
                        transactions,
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

        // Expense/Income Toggle
        ExpenseIncomeToggle(
            isIncome = isIncome,
            selected = selectedCategory,
            onSelectionChange = { newSelection ->
                selectedCategory.value = newSelection
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown to Select Transaction or Category
        Text(
            text = selectedType,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Display Transaction List or Category Progress
        if (selectedType == "Transaction") {
            RecentTransactions(
                transactions = viewModel.filterTransactionsByType(
                    transactions = transactions,
                    type = selectedCategory.value
                )
            )
        } else {
            val (categories, total) = viewModel.calculateCategoryProgress(
                type = selectedCategory.value.uppercase(),
                transactions = transactions
            )

            if (categories.isEmpty()) {
                Text("No expense data available", modifier = Modifier.fillMaxWidth().padding(16.dp))
            } else {
                CategoryProgress(categories = categories, total = total, colorMapping = categoryColorMap.value)
            }
        }
    }
}





@Composable
fun DropDownSelector(
    selectedItem: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth(0.4f)) {
        // Button to toggle the dropdown
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = mintCream,
                contentColor = Color.Black
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = selectedItem, modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon"
                )
            }
        }

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    text = { Text(option) }
                )
            }
        }
    }
}

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
                painter = painterResource(id = R.drawable.baseline_pie_chart_24, ),
                contentDescription = "Line chart",
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
                contentDescription = "Bar",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun CategoryProgress(categories: List<Pair<String, Double>>, total: Double, colorMapping: Map<String, Color>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn {
            items(categories) { (category, amount) ->
                val progress = (amount / total).toFloat() // Convert to Float for ProgressBar
                val categoryColor = colorMapping[category] ?: Color.Gray // Default to gray if not found


                Column(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    LinearProgressIndicator(
                        progress = { progress },
                        color = categoryColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                    )
                    Text(
                        text = "£${"%.2f".format(amount)}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}



@Composable
fun ExpenseIncomeToggle(
    isIncome : Boolean,
    selected: MutableState<String>,
    onSelectionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()
        .padding(16.dp))
         {
        // Expense button
        Button(
            modifier = Modifier.fillMaxWidth(0.5f),
            onClick = { onSelectionChange("Expense") },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected.value == "Expense") tealGreen else mintCream,
                contentColor =  if (selected.value == "Expense") Color.White else Color.Black
            ),
        ) {
            Text(text = "Expense")
        }

        // Income button
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSelectionChange("Income") },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected.value == "Income") tealGreen else mintCream,
                contentColor =  if (selected.value == "Income") Color.White else Color.Black
            ),
        ) {
            Text(text = "Income")
        }
    }
}




@Preview(showBackground = true)
@Composable
fun Preview() {
    // Mock ViewModel to simulate data for the preview.
    val mockViewModel = StatsViewModel(LocalContext.current)
    // Add mock transaction data here if needed.

    val navController = rememberNavController()

    XpenseAppTheme {
        // Displaying the AddScreen with mock data and a theme.
        //StatsScreen(mockViewModel, navController = navController, context = LocalContext.current)
    }
}
