package uk.ac.tees.mad.d3424757.xpenseapp.screens.addTransaction

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.components.*
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Category
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.HomeViewModel
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.TransactionViewModel

/**
 * Main Composable for adding a transaction (income or expense).
 *
 * @param navController Navigation controller for navigating back.
 * @param modifier Modifier for styling.
 * @param viewModel TransactionViewModel for managing transaction state.
 * @param isIncome Flag to differentiate income and expense.
 * @param context Context for accessing application resources.
 */
@Composable
fun AddTransaction(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: TransactionViewModel,
    isIncome: Boolean,
    context: Context
) {
    // Observing ViewModel states
    val amount by viewModel.amount.collectAsState()
    val description by viewModel.description.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var isMenuExpanded by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    // State variables to track user actions or data
    var dataSaved by remember { mutableStateOf(false) }
    var dataCancelled by remember { mutableStateOf(false) }


    // Main Screen Layout
    XHomeBackground {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            // Top Bar Section
            TopBar(
                navController = navController,
                isIncome = isIncome,
                isMenuExpanded = isMenuExpanded,
                onMenuExpand = { isMenuExpanded = it },
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Form Section
            TransactionForm(
                viewModel = viewModel,
                context = context,
                isIncome = isIncome,
                amount = amount,
                description = description,
                selectedDate = selectedDate,
                selectedCategory = selectedCategory
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            XButton(
                text = "Save",
                handleClick = {
                    if (viewModel.validateTransaction()) {
                            showDialog = true
                    }
                }
            )
            // Trigger the dialog when 'showDialog' is true
            if (showDialog) {
                SaveCancelDialog(
                    openDialog = showDialog,
                    onSave = {
                        // Handle save action
                        viewModel.addTransaction(isIncome)
                        viewModel.resetTransaction()
                        showDialog = false // Close the dialog after action
                    },
                    onCancel = {
                        // Handle cancel action
                        println("Cancel button clicked!")
                        showDialog = false // Close the dialog after action
                    },
                    onDismiss = {
                        // Optionally handle the dismiss action
                        showDialog = false // Close the dialog
                    }
                )
            }
            // Display the status based on actions
            Text(modifier = Modifier.align(Alignment.CenterHorizontally),
                text = when {
                    dataSaved -> "Data saved successfully"
                    dataCancelled -> "Data save canceled"
                    else -> ""
                }
            )
        }
    }
}

/**
 * Top bar containing navigation back and menu options.
 */
@Composable
fun TopBar(
    navController: NavController,
    isIncome: Boolean,
    isMenuExpanded: Boolean,
    onMenuExpand: (Boolean) -> Unit,
    viewModel: TransactionViewModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = "Back",
                tint = Color.White
            )
        }

        // Title
        Text(
            text = if (isIncome) "Add Income" else "Add Expense",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        // Menu Button
        IconButton(onClick = { onMenuExpand(!isMenuExpanded) }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Menu",
                tint = Color.White
            )
            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { onMenuExpand(false) }
            ) {
                    DropdownMenuItem(
                        text = { Text(text = "Clear") },
                        onClick = { onMenuExpand(false)
                             viewModel.resetTransaction()}
                    )

            }
        }
    }
}

/**
 * Form for entering transaction details (category, amount, date, description).
 */
@Composable
fun TransactionForm(
    viewModel: TransactionViewModel,
    context: Context,
    isIncome: Boolean,
    amount: String,
    description: String,
    selectedDate: String,
    selectedCategory: Category?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            // Category Field
            Text("Category", style = MaterialTheme.typography.bodyMedium)
            TransactionDropDown(
                categories = viewModel.getCategories(isIncome),
                onItemSelection = { viewModel.updateCategory(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Amount Field
            Text("Amount", style = MaterialTheme.typography.bodyMedium)
            XInputField(
                value = amount,
                onValueChange = { viewModel.updateAmount(it) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_currency_pound_24),
                        contentDescription = "Currency"
                    )
                },
                keyboardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date Picker
            Text("Date", style = MaterialTheme.typography.bodyMedium)
            XInputField(
                value = selectedDate,
                onValueChange = {},
                trailingIcon = {
                    IconButton(onClick = {
                        val calendar = Calendar.getInstance()
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                viewModel.updateDate("$dayOfMonth/${month + 1}/$year")
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                    }
                },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description Field
            Text("Description", style = MaterialTheme.typography.bodyMedium)
            XInputField(
                value = description,
                onValueChange = { viewModel.updateDescription(it) },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLine = 5,
                keyboardType = KeyboardType.Text,
                singleLine = false
            )
        }
    }
}

/**
 * Dropdown for selecting a transaction category.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDropDown(
    categories: List<Category>,
    onItemSelection: (Category) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf<Category?>(null) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {

        val displayValue = selectedItem.value?.name ?: "Select category"

        XInputField(
            value = displayValue,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            leadingIcon = { selectedItem.value?.let { CategoryIcon(it) } },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            readOnly = true
        )


        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CategoryIcon(category)
                            Spacer(Modifier.width(32.dp))
                            Text(category.name)
                        }
                    },
                    onClick = {
                        selectedItem.value = category
                        expanded = false
                        onItemSelection(category)
                    }
                )
            }
        }
    }
}

/**
 * Displays a category icon with background.
 */
@Composable
private fun CategoryIcon(category: Category) {
    Box(
        modifier = Modifier.size(40.dp).background(color = category.color.copy(alpha = 0.2f), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(painter = painterResource(id = category.icon), contentDescription = null, tint = category.color)
    }
}

/**
 * Preview function for development purposes.
 */
@Preview
@Composable
fun AddPreview() {
    // Mock ViewModel to simulate data for the preview.
    val mockViewModel = TransactionViewModel(LocalContext.current)
        // Add mock transaction data here if needed.

    val navController = rememberNavController()

    XpenseAppTheme {
        // Displaying the AddScreen with mock data and a theme.
        AddTransaction(
            modifier = Modifier,
            viewModel = mockViewModel,
            navController = navController,
            context = LocalContext.current,
            isIncome = true
        )
    }
}
