package uk.ac.tees.mad.d3424757.xpenseapp.screens.budget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.d3424757.xpenseapp.components.BottomNavigationBar
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetWithSpent
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionCategories.getIconAndColor
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.BudgetViewModel
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BudgetScreen(modifier: Modifier = Modifier, navController : NavController, context : Context) {

    // Initialize the ViewModel
    val budgetViewModel = BudgetViewModel(context = context)

    // Create state to hold current month and year
    val currentMonth = remember { mutableStateOf(Calendar.getInstance()) }

    // Get the month name
    val monthName = remember(currentMonth.value) {
        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentMonth.value.time)
    }

    // Observe budgets from the ViewModel
    val budgets by budgetViewModel.budgetsForMonth.observeAsState(emptyList())


    Scaffold(
        containerColor = tealGreen,
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        topBar = {
            // Top Navigation Row with Month and Arrows
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { // Update to previous month
                        val updatedCalendar = currentMonth.value.clone() as Calendar
                        updatedCalendar.add(Calendar.MONTH, -1)
                        currentMonth.value = updatedCalendar
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous Month",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = monthName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
                IconButton(onClick = {
                    // Update to previous month
                    val updatedCalendar = currentMonth.value.clone() as Calendar
                    updatedCalendar.add(Calendar.MONTH, 1)
                    currentMonth.value = updatedCalendar
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next Month",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)

                    )
                }
            }
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(mintCream)
            ) {
                Scaffold(bottomBar = {
                    XButton(
                        modifier = Modifier.padding(32.dp),
                        text = "Create a budget",
                        handleClick = {}
                    )
                }) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (budgets.isEmpty()) {
                            // No Budget Message
                            Text(
                                text = "You do not have a budget.\nLet's make one so you can be in control.",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        } else {
                            LazyColumn(modifier = Modifier.size(450.dp)) {
                                items(budgets) { budget ->
                                    BudgetItem(budget)

                                }
                            }
                        }
                    }

                }
            }
        }
    }
}


// Budget Item Composable
@Composable
fun BudgetItem(budget: BudgetWithSpent) {
    val exceeded = budget.spent > budget.amount
    Column(modifier = Modifier
        .padding(top = 8.dp, bottom = 40.dp)
        .clickable {  }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                getIconAndColor(budget.category)?.let {
                    Modifier
                        .size(12.dp)
                        .background(it.second, CircleShape)
                }?.let {
                    Box(
                        modifier = it
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = budget.category,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            if (exceeded) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Exceeded Budget",
                    tint = Color.Red
                )
            }
        }
        Text(
            text = "Remaining: $${"%.2f".format(budget.amount - budget.spent)}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        getIconAndColor(budget.category)?.let {
            LinearProgressIndicator(
                progress = { (budget.spent / budget.amount).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = it.second,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "$${"%.2f".format(budget.spent)} of $${"%.2f".format(budget.amount)}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        if (exceeded) {
            Text(
                text = "You have exceeded the limit.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red
            )
        }

    }
}




@Preview(showBackground = true)
@Composable
fun AddPreview() {
    // Mock ViewModel to simulate data for the preview.
    val mockViewModel = TransactionViewModel(LocalContext.current).apply {
        // Add mock transaction data here if needed.
    }
    val navController = rememberNavController()

    XpenseAppTheme {
        // Displaying the AddScreen with mock data and a theme.
        BudgetScreen(navController = navController, context = LocalContext.current)
    }
}
