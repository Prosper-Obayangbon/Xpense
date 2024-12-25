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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.ExpenseBackground
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.IconColor
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.IncomeBackground
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.TransactionViewModel

/**
 * Composable function representing the "Add" screen, where users can add income or expense
 * entries and view recently added transactions.
 *
 * @param modifier The Modifier for styling this composable.
 * @param viewModel The HomeViewModel to observe transaction data.
 * @param navController The NavController for navigating between screens.
 */
@Composable
fun AddScreen(
    modifier: Modifier,
    viewModel: TransactionViewModel,
    navController: NavController,
) {
    // Observing the transactions state from the ViewModel.
    val transactions by viewModel.transactions.collectAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding()
            .background(mintCream)
    ) {
        XTopBar(
            modifier = Modifier.fillMaxWidth(),
            text = "Add",
            textColor = Color.Black,
            backClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Row containing buttons for adding income and expenses.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Button for adding income.
            AddButton(
                label = "Add Income",
                backgroundColor = IncomeBackground,
                iconColor = IconColor,
                navController = navController,
                isIncome = true
            )
            // Button for adding expense.
            AddButton(
                label = "Add Expense",
                backgroundColor = ExpenseBackground,
                iconColor = IconColor,
                navController = navController
            )
        }

        // Section heading for recent transactions.
        Text(
            text = "Last Added",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Conditional rendering for recent transactions.
        if (transactions.isEmpty()) {
            // Placeholder message when no transactions exist.
            Text(
                text = "No transactions yet!",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            // Displays the list of recent transactions.
            RecentTransactions(transactions = transactions)
        }
    }
}

/**
 * Composable function representing a button for adding an income or expense.
 *
 * @param label The text label for the button.
 * @param backgroundColor The background color of the button.
 * @param iconColor The color of the icon displayed on the button.
 * @param navController The NavController to handle navigation.
 * @param isIncome Boolean flag indicating if the button is for income or expense.
 */
@Composable
fun AddButton(
    label: String,
    backgroundColor: Color,
    iconColor: Color,
    navController: NavController,
    isIncome: Boolean = false // Defaults to false for expense buttons.
) {
    Box(
        modifier = Modifier
            .width(185.dp)
            .height(115.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable {
                // Navigates to the AddTransaction screen with an argument indicating income/expense.
                navController.navigate("${XpenseScreens.AddTransaction.route}?isIncome=$isIncome")

            },
        contentAlignment = Alignment.Center // Aligns content to the center.
    ) {
        // Column containing the icon and label for the button.
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon representing the button's purpose (income/expense).
            if (isIncome) {
                Icon(
                    imageVector = Icons.Filled.AddCircle, // Vector asset for income
                    contentDescription = "Add Income",
                    tint = iconColor,
                    modifier = Modifier.size(50.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_remove_circle_24),
                    contentDescription = "Add Expense",
                    tint = iconColor,
                    modifier = Modifier.size(50.dp)
                )
            }

            // Text label for the button.
            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

/**
 * Preview composable for the AddScreen, displaying it in a mocked environment.
 */
@Preview
@Composable
fun AddPreview() {
    // Mock ViewModel to simulate data for the preview.
    val mockViewModel = TransactionViewModel(LocalContext.current).apply {
        // Add mock transaction data here if needed.
    }
    val navController = rememberNavController()

    XpenseAppTheme {
        // Displaying the AddScreen with mock data and a theme.
        AddScreen(
            modifier = Modifier,
            viewModel = mockViewModel,
            navController = navController
        )
    }
}
