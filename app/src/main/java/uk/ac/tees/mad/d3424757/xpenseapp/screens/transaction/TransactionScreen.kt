import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.components.BottomNavigationBar
import uk.ac.tees.mad.d3424757.xpenseapp.components.XDropDownSelector
import uk.ac.tees.mad.d3424757.xpenseapp.components.TransactionItem
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.monthFilter
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionCategories.getIconAndColor
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionFilters
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionType
import uk.ac.tees.mad.d3424757.xpenseapp.utils.formatAmount
import uk.ac.tees.mad.d3424757.xpenseapp.utils.groupTransactionsByDate
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.TransactionViewModel

/**
 * Composable function that represents the Transaction Screen where users can view their transactions,
 * apply filters (by month and type), and navigate to other parts of the app like the financial report.
 *
 * @param modifier Modifier for styling the composable.
 * @param viewModel The ViewModel for managing and providing transaction data.
 * @param navController The NavController for navigating between screens.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionScreen(
    modifier: Modifier,
    viewModel: TransactionViewModel,
    navController: NavController
) {
    // Fetch transactions from the viewModel
    val transactions by viewModel.transactions.collectAsState(emptyList())

    // Month and type filter options
    val types = listOf("All", "Income", "Expense")


    var filterExpanded by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf(monthFilter.first()) }
    var selectedType by remember { mutableStateOf(types.first()) }

    // Grouped transactions by date (initially done here without filters)
    val groupedTransactions = remember(transactions) {
        groupTransactionsByDate(transactions)
    }

    // Apply filters whenever selectedMonth or selectedType changes
    val filteredTransactions = remember(selectedMonth, selectedType, transactions) {
        var result = groupedTransactions // Start with grouped transactions

        // Apply month filter if it's not "All"
        if (selectedMonth != "All") {
            val monthIndex = monthFilter.indexOf(selectedMonth)  // Get the month index (1-based)
            result = TransactionFilters.filterByMonth(transactions, monthIndex)  // Filter by month
        }

        // Apply type filter if it's not "All"
        if (selectedType != "All") {
            val type = when (selectedType) {
                "Income" -> TransactionType.INCOME
                "Expense" -> TransactionType.EXPENSE
                else -> throw IllegalArgumentException("Invalid transaction type")
            }
            result = TransactionFilters.filterByType(result, type) // Filter by type
        }

        // Group filtered transactions again after applying filters
        result
    }

    Scaffold(bottomBar = {
        // Bottom navigation
        BottomNavigationBar(navController = navController)
    },
        ) {
        // UI Layout for the Transaction Screen
        Column(modifier = Modifier
            .background(color = mintCream)){

            Box(modifier.fillMaxSize()
            ){
                Column {
                    // Filter Dropdowns (Month and Type)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        XDropDownSelector(
                            selectedItem = selectedMonth,
                            options = monthFilter,
                            onOptionSelected = {selectedMonth = it}
                        )

                        FilterDropdownMenu(
                            types = types,
                            onTypeSelected = { selectedType = it },
                            expanded = filterExpanded,
                            onExpandedChange = { filterExpanded = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Financial report link
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clickable {navController.navigate(XpenseScreens.StatsScreen.route) }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "See your financial Report", color = tealGreen)
                        Icon(painter = painterResource(R.drawable.keyboard_arrow_right), contentDescription = null, tint = tealGreen)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Display the grouped transactions
                    GroupedTransactionList(groupedTransactions = filteredTransactions, navController = navController)

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }





        }
    }

}


/**
 * Composable function for displaying the transaction type filter dropdown menu in the TransactionScreen.
 *
 * @param types The list of available transaction types (e.g., "All", "Income", "Expense").
 * @param selectedType The currently selected transaction type.
 * @param onTypeSelected A callback for handling the type selection.
 * @param expanded State of the dropdown (whether it's expanded or not).
 * @param onExpandedChange A callback to change the expanded state of the dropdown.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdownMenu(
    types: List<String>,
    onTypeSelected: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        // Icon for the dropdown
        Icon(
            painter = painterResource(id = R.drawable.filter_list),
            contentDescription = "Filter dropdown",
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .menuAnchor() // Position the icon correctly within the ExposedDropdownMenuBox
        )

        // Dropdown menu containing the filter options
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) } // Close the menu when dismissed
        ) {
            // Iterate through filter types and create a menu item for each
            types.forEach { filter ->
                DropdownMenuItem(
                    onClick = {
                        onTypeSelected(filter) // Update the selected filter
                        onExpandedChange(false) // Close the dropdown menu
                    },
                    text = {
                        Text(text = filter) // Display each filter type in the menu
                    }
                )
            }
        }
    }
}


/**
 * Composable function for displaying a list of grouped transactions based on the selected filters.
 *
 * @param groupedTransactions The transactions grouped by categories such as "Today", "Yesterday", etc.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupedTransactionList(groupedTransactions: Map<String, List<TransactionData>>, navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxWidth()
        .height(500.dp)
        .padding(8.dp)) {
        // Iterate over the grouped transactions and display each group
        groupedTransactions.forEach { (header, transactionList) ->
            item {
                // Add the category header
                if (transactionList.isNotEmpty()) {
                    // Display the category header
                    Text(
                        text = header, // The header for each group (Today, Yesterday, etc.)
                        style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Display the transactions for this group
                    transactionList.reversed().forEach { transaction ->
                        val image = getIconAndColor(categoryName = transaction.category)

                        if (image != null) {
                            TransactionItem(
                                modifier = Modifier.clickable {
                                    navController.navigate("transaction/${transaction.id}")
                                },
                                itemName = transaction.category,
                                description = transaction.description,
                                amount = formatAmount(transaction.amount, transaction.type.displayName),
                                time = transaction.time,
                                icon = image.first,
                                iconColor = image.second
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun Preview() {
    // Mock ViewModel to simulate data for the preview.
    val mockViewModel = TransactionViewModel(LocalContext.current)
    // Add mock transaction data here if needed.

    val navController = rememberNavController()

    XpenseAppTheme {
        // Displaying the AddScreen with mock data and a theme.
        TransactionScreen(
            modifier = Modifier,
            viewModel = mockViewModel,
            navController = navController,
        )
    }
}
