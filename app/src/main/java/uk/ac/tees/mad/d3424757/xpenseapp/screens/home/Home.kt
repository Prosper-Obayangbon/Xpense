package uk.ac.tees.mad.d3424757.xpenseapp.screens.home

import HomeViewModel
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.components.BottomNavigationBar
import uk.ac.tees.mad.d3424757.xpenseapp.components.RecentTransactions
import uk.ac.tees.mad.d3424757.xpenseapp.components.TransactionItem
import uk.ac.tees.mad.d3424757.xpenseapp.components.XHomeBackground
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.utils.formatAmount
import java.time.LocalTime

/**
 * Home Screen Composable displaying the dashboard, transactions, and other key details.
 *
 * @param modifier Modifier to apply additional customizations to the layout.
 * @param viewModel HomeViewModel instance to manage the app's state and data.
 * @param navController NavController instance for managing navigation within the app.
 */
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(
    modifier: Modifier,
    viewModel: HomeViewModel,
    navController: NavController
) {
    val transactions by viewModel.transactions.collectAsState(emptyList())

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
            )
        }
    ) {
        XHomeBackground {
            Column(
                modifier = modifier
            ) {
                Box(modifier = Modifier.size(755.dp)) {
                    Column {
                        HeaderSection(viewModel)
                        BalanceCard(viewModel, transactions)
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Recent Transactions",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )

                                Button(
                                    onClick  = { navController.navigate(XpenseScreens.TransactionScreen.route) },
                                    colors = ButtonDefaults.buttonColors(mintCream),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                                    elevation = ButtonDefaults.buttonElevation(0.dp)
                                ) {
                                    Text("See All", color = tealGreen)
                                }
                            }
                        }
                        RecentTransactions(transactions)
                    }
                }
            }
        }
    }
}

/**
 * Header section displaying a greeting message and notification button.
 *
 * @param viewModel HomeViewModel instance to access user data and greetings.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeaderSection(viewModel: HomeViewModel) {
    val userName by viewModel.userName.collectAsState()
    val greeting = viewModel.getGreeting()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Column {
            Text(text = greeting, fontSize = 16.sp, color = Color.White)
            Text(
                text = userName.ifEmpty { "User" },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = {},
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.notification),
                contentDescription = "Notification Icon",
                tint = Color.White
            )
        }
    }
}

/**
 * Balance card showing the total balance and a summary of income and expenses.
 *
 * @param viewModel HomeViewModel instance to access balance calculation methods.
 * @param list List of transactions to calculate the total balance, income, and expenses.
 */
@Composable
fun BalanceCard(viewModel: HomeViewModel, list: List<TransactionData>) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(tealGreen),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(200.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Total Balance", color = Color.White, fontSize = 14.sp)
                    Text(
                        text = viewModel.calculateBalance(list),
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.more_horiz),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CardRowItem(title = "Income",
                    amount = viewModel.getTotalIncome(list),
                    image = R.drawable.arrow_downward)
                CardRowItem(title = "Expenses",
                    amount = viewModel.getTotalExpense(list),
                    image = R.drawable.arrow_upward)
            }
        }
    }
}

/**
 * A row item for displaying a single card value (e.g., Income or Expenses).
 *
 * @param title The title for the row item (either "Income" or "Expenses").
 * @param amount The amount associated with the title (calculated value).
 * @param image The image resource representing the type (e.g., income or expense icon).
 */
@Composable
fun CardRowItem(title: String, amount: String, image: Int) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color.White.copy(alpha = 0.2f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, color = Color.White, fontSize = 16.sp)
        }
        Text(
            text = amount,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}
