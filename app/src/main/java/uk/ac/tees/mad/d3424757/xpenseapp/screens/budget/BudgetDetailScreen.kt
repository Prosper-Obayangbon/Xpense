package uk.ac.tees.mad.d3424757.xpenseapp.screens.budget

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.d3424757.xpenseapp.components.SaveCancelDialog
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetData
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionCategories.getIconAndColor
import uk.ac.tees.mad.d3424757.xpenseapp.utils.getCurrentDate
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.BudgetViewModel

/**
 * BudgetDetailScreen displays detailed information about a specific budget,
 * including remaining amount, progress, and the ability to delete the budget.
 *
 * @param modifier Modifier to customize the appearance and behavior of the screen.
 * @param budgetId The ID of the budget to display details for.
 * @param navController The navigation controller to navigate between screens.
 * @param budgetViewModel The ViewModel responsible for managing budget-related data.
 */
@Composable
fun BudgetDetailScreen(
    modifier: Modifier = Modifier,
    budgetId: String,
    navController: NavController,
    budgetViewModel: BudgetViewModel
) {
    // Collecting the list of budgets from the ViewModel
    val budgets by budgetViewModel.calculatedBudgets.collectAsState()

    // Filtering the specific budget based on the provided budgetId
    val budget = budgets.find { it.id.toString() == budgetId }

    // Dialog state to show confirmation before deleting the budget
    var showDialog by remember { mutableStateOf(false) }

    // If the budget is not found, return early
    if (budget == null) {
        return
    }

    // Layout for the Budget Detail screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mintCream)
            .padding(16.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = mintCream),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Row: Back and Delete Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button to navigate to the previous screen
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                // Title text in the center of the top row
                Text(
                    text = "Detail Budget",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                // Delete button to show a confirmation dialog before deletion
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Budget",
                        tint = Color.Black
                    )
                }
            }

            // Confirmation dialog to confirm or cancel the delete action
            if (showDialog) {
                SaveCancelDialog(
                    openDialog = showDialog,
                    onSave = {
                        // Delete the budget and navigate back
                        budgetViewModel.deleteBudget(budgetId.toInt())
                        navController.popBackStack()
                    },
                    onCancel = { showDialog = false },
                    onDismiss = { showDialog = false }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Get icon and color for the category
            val categoryIcon = getIconAndColor(budget.category)

            // Category icon and background
            if (categoryIcon != null) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(categoryIcon.second.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    // Display the category icon
                    Icon(
                        painter = painterResource(id = categoryIcon.first),
                        contentDescription = budget.category,
                        tint = categoryIcon.second,
                        modifier = Modifier.size(100.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display the category name
            Text(
                text = budget.category,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display remaining budget amount
            Text(
                text = "Remaining",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                fontSize = 20.sp
            )
            val remainder = budget.amount - budget.spent
            Text(
                text = "£${"%.2f".format(if (remainder > 0) remainder else 0.0)}",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            // Progress bar showing how much of the budget has been spent
            if (categoryIcon != null) {
                LinearProgressIndicator(
                    progress = (budget.spent / budget.amount).toFloat().coerceIn(0f, 1f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = categoryIcon.second
                )
            }

            // Warning if the budget has been exceeded or is exactly equal to the total amount
            if (budget.spent > budget.amount || budget.spent == budget.amount) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .background(MaterialTheme.colorScheme.errorContainer, shape = RoundedCornerShape(16.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Warning icon
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Warning message
                    Text(
                        text = "You've exceeded the limit",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Button to navigate to the "Edit" screen for this budget
            XButton(text = "Edit") { navController.navigate("addBudget/${true}/${budget.id}") }
        }
    }
}

/**
 * Preview of the BudgetDetailScreen for design-time visualization.
 *
 * @param modifier Modifier to customize the appearance of the preview screen.
 */
@Preview(showBackground = true)
@Composable
fun PreviewBudgetDetailScreen() {
    val context = LocalContext.current
    BudgetDetailScreen(
        modifier = Modifier,
        budgetId = "1",
        navController = NavController(context),
        budgetViewModel = BudgetViewModel(context)
    )
}
