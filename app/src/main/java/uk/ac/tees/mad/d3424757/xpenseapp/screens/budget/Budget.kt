package uk.ac.tees.mad.d3424757.xpenseapp.screens.budget

import androidx.collection.mutableIntListOf
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.d3424757.xpenseapp.components.BottomNavigationBar
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.TransactionViewModel

@Composable
fun BudgetScreen(modifier: Modifier = Modifier, navController : NavController) {
    Scaffold(
        modifier = modifier,
        containerColor = tealGreen,
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        topBar = {
            // Top Navigation Row with Month and Arrows
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Handle previous month */ }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous Month",
                        tint = Color.White
                    )
                }
                Text(
                    text = "May",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                IconButton(onClick = { /* Handle next month */ }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next Month",
                        tint = Color.White
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


            // Card for Budget Section
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(mintCream)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val budgets = listOf(
                        Budget("Shopping", 1000.0, 1200.0, Color.Yellow),
                        Budget("Transportation", 700.0, 350.0, Color.Blue),
                        Budget("Transportation", 700.0, 350.0, Color.Blue),
                        Budget("Transportation", 700.0, 350.0, Color.Blue),
                        Budget("Transportation", 700.0, 350.0, Color.Blue),
                        Budget("Transportation", 700.0, 350.0, Color.Blue),
                        Budget("Transportation", 700.0, 350.0, Color.Blue),
                        Budget("Transportation", 700.0, 350.0, Color.Blue),
                        Budget("Transportation", 700.0, 350.0, Color.Blue),
                    )

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
                            item {
                                budgets.forEach { budget ->
                                    BudgetItem(budget)
                                }

                            }
                        }
                    }
                    XButton(
                        modifier = Modifier.padding(16.dp),
                        text = "Create a budget",
                        handleClick = {}
                    )


                }
            }
        }
    }
}

// Model for Budget Item
data class Budget(val category: String, val budget: Double, val spent: Double, val color: Color)

// Budget Item Composable
@Composable
fun BudgetItem(budget: Budget) {
    val exceeded = budget.spent > budget.budget
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
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(budget.color, CircleShape)
                )
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
            text = "Remaining: $${"%.2f".format(budget.budget - budget.spent)}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        LinearProgressIndicator(
            progress = { (budget.spent / budget.budget).toFloat().coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = budget.color,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "$${"%.2f".format(budget.spent)} of $${"%.2f".format(budget.budget)}",
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
        BudgetScreen(navController = navController)
    }
}
