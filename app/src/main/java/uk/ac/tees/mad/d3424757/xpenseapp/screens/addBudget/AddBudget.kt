package uk.ac.tees.mad.d3424757.xpenseapp.screens.addBudget

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
import androidx.compose.ui.platform.LocalContext
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
 * Composable function to create a budget screen.
 * Includes user input for budget amount, category selection, and alert preferences.
 *
 * @param modifier A Modifier instance to style the composable.
 * @param viewModel The ViewModel instance managing the screen's state.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun AddBudget(modifier: Modifier = Modifier, context : Context, navController: NavController, isEdit : Boolean, budgetId : Int) {

    val viewModel = BudgetViewModel(context)

    val errorMessage by viewModel.errorMessage.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()


    // Initialize the budget for editing if isEdit is true
    if (isEdit) {
        viewModel.initializeBudget(budgetId)
    }


    Scaffold(
        topBar = {
            XTopBar(
                modifier = modifier,
                text = if(isEdit) "Edit Budget" else "Create Budget",
                backClick = {navController.popBackStack()}
            )
        },
        bottomBar = {
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

        BudgetContent(viewModel = viewModel)

    }

}

/**
 * The content of the budget creation screen.
 * Displays input for budget amount.
 */
@Composable
fun BudgetContent(viewModel: BudgetViewModel) {
    Box(modifier = Modifier.background(tealGreen)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Heading Text
            Text(
                "How much do you want to spend?",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                color = Color.LightGray
            )

            // Budget Amount Input Field
            TextField(
                value = viewModel.budgetAmount.toString(),
                onValueChange = {
                    viewModel.onBudgetAmountChange(it.toDouble())
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // Transparent for focused
                    unfocusedContainerColor = Color.Transparent // Transparent for unfocused
                ),
                leadingIcon = {
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
 * The bottom bar of the budget creation screen.
 * Includes category selection, alert toggle, and alert threshold slider.
 *
 * @param viewModel The ViewModel instance managing the state.
 * @param selectedCategory A mutable state holding the selected category.
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
    // Dialog State
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        colors = CardDefaults.cardColors(mintCream)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Category Dropdown Selector
            XDropDownSelector(
                selectedItem = selectedCategory,
                options = viewModel.getCategories(),
                onOptionSelected = onCategorySelected,
                fraction = 1f
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Alert Toggle
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
                    checked = viewModel.isAlertEnabled,
                    onCheckedChange = { viewModel.onAlertToggle(it) },
                    colors = SwitchDefaults.colors(tealGreen)
                )
            }

            if (viewModel.isAlertEnabled) {
                Spacer(modifier = Modifier.height(8.dp))
                val value = viewModel.alertThreshold.toFloat()
                Slider(
                    value = value,
                    onValueChange = { viewModel.onAlertThresholdChange(it.toInt()) },
                    valueRange = 0f..100f,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        activeTrackColor = tealGreen,
                        thumbColor = tealGreen
                    ),
                    thumb = {
                        // Custom thumb as a rectangle
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(50.dp, 30.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(tealGreen)
                        ) {
                            Text(
                                text = "${value.toInt()}%",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                )
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            XButton(text = "Continue") {
                if (viewModel.validateBudget()) {
                    showDialog = true
                }
            }
            // Trigger SaveCancelDialog when `showDialog` is true
            if (showDialog) {
                SaveCancelDialog(
                    openDialog = showDialog,
                    onSave = {
                        val budget = BudgetData(
                            id = if (isEdit) viewModel.currentId else 0, // Use existing ID for editing, 0 for new budget
                            category = selectedCategory,
                            amount = viewModel.budgetAmount,
                            alertEnabled = viewModel.isAlertEnabled,
                            alertThreshold = (viewModel.budgetAmount.toInt() * viewModel.alertThreshold / 100),
                            date = getCurrentDate()
                        )
                        if (isEdit) {
                            viewModel.updateBudget(budget) // Update existing budget
                        } else {
                            viewModel.saveBudget(budget) // Save new budget
                        }
                        navController.popBackStack() // Navigate back after saving
                    },
                    onCancel = { showDialog = false },
                    onDismiss = { showDialog = false }
                )
            }

        }
    }
}
