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
import androidx.compose.material.icons.filled.ShoppingCart
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
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionCategories.getIconAndColor
import uk.ac.tees.mad.d3424757.xpenseapp.utils.getCurrentDate
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.BudgetViewModel

@Composable
fun BudgetDetailScreen(
    modifier: Modifier = Modifier,
    budgetId: String,
    navController: NavController,
    budgetViewModel: BudgetViewModel
) {
    val budgets by budgetViewModel.calculatedBudgets.collectAsState()

    // Filter the specific budget using the budgetId
    val budget = budgets.find { it.id.toString() == budgetId }

    // Dialog State
    var showDialog by remember { mutableStateOf(false) }

    // Safe handling of null budget
    if (budget == null) {
        // Handle the case where the budget is not found (e.g., show a loading indicator or an error message)
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mintCream)
            .padding(16.dp) // Added padding here
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
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "Detail Budget",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = {showDialog =  true}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Budget",
                        tint = Color.Black
                    )
                }
            }

            if (showDialog) {
                SaveCancelDialog(
                    openDialog = showDialog,
                    onSave = {
                        budgetViewModel.deleteBudget(budgetId.toInt())
                        navController.popBackStack() // Navigate back after saving
                    },
                    onCancel = { showDialog = false },
                    onDismiss = { showDialog = false }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val categoryIcon = getIconAndColor(budget.category) // Get icon and color for category


            // Category with Icon
            if (categoryIcon != null) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(categoryIcon.second.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = categoryIcon.first),
                        contentDescription = budget.category,
                        tint = categoryIcon.second,
                        modifier = Modifier.size(100.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = budget.category,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Remaining Amount
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

            // Progress Bar
         