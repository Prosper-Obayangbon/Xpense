package uk.ac.tees.mad.d3424757.xpenseapp.screens

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
import androidx.compose.ui.graphics.Color
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
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.MONTH_FILTER
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionCategories.getIconAndColor
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionFilters
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionType
import uk.ac.tees.mad.d3424757.xpenseapp.utils.formatAmount
import uk.ac.tees.mad.d3424757.xpenseapp.utils.groupTransactionsByDate
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.TransactionViewModel

/**
 * TransactionScreen composable function for displaying user transactions.
 * Allows applying filters like month and transaction type.
 *
 * @param modifier Modifier for styling the composable.
 * @param viewModel The ViewModel providing transaction data.
 * @param navController The NavController for navigation between screens.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionScreen(
    modifier: Modifier,
    viewModel: TransactionViewModel,
    navController: NavController
) {
    // Fetch the list of transactions from the ViewModel
    val transactions by viewModel.transactions.collectAsState(emptyList())

    // Filter options for transaction type and month
    val types = Constants.TRANSACTION_TYPES

    // State variables for managing the filter UI and selected values
    var filterExpanded by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf(MONTH_FILTER.first()) }
    var selectedType by remember { mutableStateOf(types.first()) }

    // Group transactions by date, initializing the grouped data
    val groupedTransactions = remember(transactions) {
        groupTransactionsByDate(transactions)
    }

    val filteredTransactions = remember(selectedMonth, selectedType, transactions) {
        var result = groupedTransactions // Start with grouped transactions

        // Apply month filter if selectedMonth is not "All"
        if (selectedMonth != "All") {
            val monthIndex = MONTH_FILTER.indexOf(selectedMonth)
            val formattedMonthIndex = monthIndex.toString().padStart(2, '0')
            result = TransactionFilters.filterByMonth(transactions, formattedMonthIndex)
        }

        // Apply type filter if selectedType is not "All"
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
        // Bottom navigation bar
        BottomNavigationBar(navController = navController)
    }) {
        // Layout for the Transaction Screen
        Column(modifier = Modifier
            .background(color = mintCream)) {
            Box(modifier.fillMaxSize()) {
                Column {
                    // Filter Dropdowns for Month and Type
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Month Dropdown selector
                        XDropDownSelector(
                            selectedItem = selectedMonth,
                            options = MONTH_FILTER,
                            onOptionSelected = { selectedMonth = it }
                        )

                        // Transaction Type filter dropdown
                        FilterDropdownMenu(
                            types = types,
                            onTypeSelected = { selectedType = it },
                            expanded = filterExpanded,
                            onExpandedChange = { filterExpanded = it }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Link to financial report screen
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(XpenseScreens.StatsScreen.route) }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "See your financial Report", color = tealGreen)
                        Icon(painter = painterResource(R.drawable.keyboard_arrow_right), contentDescription = null, tint = tealGreen)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Display the filtered and grouped transactions
                    if(filteredTransactions.isNotEmpty()) {
                        GroupedTransactionList(
                            groupedTransactions = filteredTransactions,
                            navController = navController
                        )
                    }else{
                        Text(modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = "No Transaction data Available!!",
                            color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

/**
 * Composable function for displaying the transaction type filter dropdown menu.
 *
 * @param types List of transaction types (e.g., "All", "Income", "Expense").
 * @param onTypeSelected Callback for handling the type selection.
 * @param expanded State of the dropdown (whether it's expanded or not).
 * @param onExpandedChange Callback to change the expanded state of the dropdown.
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
        // Icon for the dropdown menu
        Icon(
            painter = painterResource(id = R.drawable.filter_list),
            contentDescription = "Filter dropdown",
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .menuAnchor() // Position the icon correctly within the ExposedDropdownMenuBox
        )

        // Dropdown menu containing filter options
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
                        Text(text = filter) // Display each filter option
                    }
                )
            }
        }
    }
}

/**
 * Composable function for displaying a list of grouped transactions.
 *
 * @param groupedTransactions Transactions grouped by categories like "Today", "Yesterday", etc.
 * @param navController The NavController for navigation between transaction details.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupedTransactionList(groupedTransactions: Map<String, List<TransactionData>>, navController: NavController) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        // Iterate over the grouped transactions and display each group
        groupedTransactions.forEach { (header, transactionList) ->
            item {
                // Display category header for each group (Today, Yesterday, etc.)
                if (transactionList.isNotEmpty()) {
                    Text(
                        text = header,
                        style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Display the transactions in this group
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

    val navController = rememberNavController()

    XpenseAppTheme {
        TransactionScreen(
            modifier = Modifier,
            viewModel = mockViewModel,
            navController = navController,
        )
    }
}
