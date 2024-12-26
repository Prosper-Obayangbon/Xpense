package uk.ac.tees.mad.d3424757.xpenseapp.screens.budget

import XTopBar
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.components.SaveCancelDialog
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.XDropDownSelector
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetData
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.utils.getCurrentDate
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.BudgetViewModel

/**
 * Composable function to create or edit a budget.
 * Includes user input for budget amount, category selection, and alert preferences.
 *
 * @param modifier A Modifier instance to style the composable.
 * @param budgetViewModel ViewModel to manage budget-related data.
 * @param navController The NavController instance for navigation.
 * @param isEdit Flag to determine if this is an edit or create operation.
 * @param budgetId The ID of the budget to edit, only used when isEdit is true.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun AddBudget(modifier: Modifier = Modifier,viewModel: BudgetViewModel, navController: NavController, isEdit : Boolean, budgetId : Int) {



    // Observe error messages and selected category from ViewModel
    val errorMessage by viewModel.errorMessage.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    // Initialize the budget for editing if isEdit is true
    if (isEdit) {
        viewModel.initializeBudget(budgetId)
    }

    // Scaffold layout for top bar and bottom bar
    Scaffold(
        topBar = {
            XTopBar(
                modifier = modifier,
                text = if(isEdit) "Edit Budget" else "Create Budget", // Title based on edit or create
                backClick = { navController.popBackStack() } // Back navigation
            )
        },
        bottomBar = {
            // Bottom bar for category selection, alert toggle, and continue button
            BudgetBottomBar(
                viewModel = viewModel,
                selectedCategory = selectedCategory,
                onCategorySelected = { viewModel.onCategoryChange(it) },
                navController = navController,
                errorMessage = errorMessage,
                isEdit = isEdit
            )
        }
    ) {
        // Content of the budget creation screen (input fields)
        BudgetContent(viewModel = viewModel)
    }
}

/**
 * Composable function to display the budget input form.
 * Displays input for the budget amount.
 */
@Composable
fun BudgetContent(viewModel: BudgetViewModel) {
    // Box layout with background color
    Box(modifier = Modifier.background(tealGreen)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Padding around the column
            verticalArrangement = Arrangement.Center, // Center content vertically
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            // Heading text
            Text(
                "How much do you want to spend?",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                color = Color.LightGray
            )

            // Budget Amount Input Field
            TextField(
                value = viewModel.budgetAmount.toString(), // Current budget amount
                onValueChange = {
                    // Update the budget amount in the ViewModel when the user types
                    viewModel.onBudgetAmountChange(it.toDouble())
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number), // Number input
                textStyle = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // Transparent background when focused
                    unfocusedContainerColor = Color.Transparent // Transparent background when unfocused
                ),
                leadingIcon = {
                    // Currency icon for the budget input field
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_currency_pound_24),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            )
        }
    }
}

/**
 * Composable function for the bottom bar in the budget screen.
 * Allows category selection, alert toggle, and the "Continue" button.
 *
 * @param viewModel The ViewModel instance managing the state.
 * @param selectedCategory The currently selected category.
 * @param onCategorySelected Callback for when the category is changed.
 * @param navController The NavController instance for navigation.
 * @param errorMessage The error message to display.
 * @param isEdit Flag to determine if this is an edit operation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetBottomBar(
    viewModel: BudgetViewModel,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    navController: NavController,
    errorMessage: String,
    isEdit: Boolean
) {
    // Dialog state to control the visibility of the Save/Cancel dialog
    var showDialog by remember { mutableStateOf(false) }

    // Card for the bottom bar with category selection and alert preferences
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        colors = CardDefaults.cardColors(mintCream) // Background color of the card
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Bottom // Align content to the bottom
        ) {
            // Category selection dropdown
            XDropDownSelector(
                selectedItem = selectedCategory,
                options = viewModel.getCategories(),
                onOptionSelected = onCategorySelected,
                fraction = 1f // Full width for dropdown
            )

            Spacer(modifier = Modifier.height(16.dp)) // Spacer between elements

            // Alert toggle
            Text("Receive Alert", style = TextStyle(fontWeight = FontWeight.Bold))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Receive alert when it reaches\nsome point",
                    style = TextStyle(color = Color.Gray)
                )
                Switch(
                    checked = viewModel.isAlertEnabled, // Current alert toggle state
                    onCheckedChange = { viewModel.onAlertToggle(it) }, // Update state on toggle
                    colors = SwitchDefaults.colors(tealGreen) // Custom color for the switch
                )
            }

            // Show alert threshold slider only if alerts are enabled
            if (viewModel.isAlertEnabled) {
                Spacer(modifier = Modifier.height(8.dp))
                val value = viewModel.alertThreshold.toFloat() // Convert alert threshold to float for slider
                Slider(
                    value = value,
                    onValueChange = { viewModel.onAlertThresholdChange(it.toInt()) }, // Update threshold
                    valueRange = 0f..100f, // Slider range from 0% to 100%
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        activeTrackColor = tealGreen, // Slider track color
                        thumbColor = tealGreen // Slider thumb color
                    ),
                    thumb = {
                        // Custom thumb design for the slider
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(50.dp, 30.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(tealGreen)
                        ) {
                            Text(
                                text = "${value.toInt()}%", // Display the percentage on the thumb
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                )
            }

            // Display error message if present
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp)) // Spacer before the Continue button

            // Continue button to proceed after validation
            XButton(text = "Continue") {
                if (viewModel.validateBudget()) { // Validate budget before proceeding
                    showDialog = true
                }
            }

            // Show Save/Cancel dialog when user clicks Continue
            if (showDialog) {
                SaveCancelDialog(
                    openDialog = showDialog,
                    onSave = {
                        val budget = BudgetData(
                            id = if (isEdit) viewModel.currentId else 0, // Use existing ID for editing, 0 for new
                            category = selectedCategory,
                            amount = viewModel.budgetAmount,
                            alertEnabled = viewModel.isAlertEnabled,
                            alertThreshold = (viewModel.budgetAmount.toInt() * viewModel.alertThreshold / 100), // Calculate alert threshold
                            date = getCurrentDate() // Current date for the budget
                        )
                        if (isEdit) {
                            viewModel.updateBudget(budget) // Update existing budget
                        } else {
                            viewModel.saveBudget(budget) // Save new budget
                        }
                        navController.popBackStack() // Navigate back after saving
                    },
                    onCancel = { showDialog = false }, // Cancel the dialog
                    onDismiss = { showDialog = false } // Dismiss the dialog when closed
                )
            }
        }
    }
}
