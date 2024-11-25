package uk.ac.tees.mad.d3424757.xpenseapp.screens.transaction

import XTopBar
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionCategories.getIconAndColor
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionType
import uk.ac.tees.mad.d3424757.xpenseapp.utils.formatAmount
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.TransactionViewModel


@Composable
fun TransactionDetailsScreen(
    modifier: Modifier = Modifier,
    transactionId : String,
    navController: NavController,
    viewModel: TransactionViewModel
) {

    val transactions by viewModel.transactions.collectAsState(emptyList())

    // Filter the specific transaction using the transactionId
    val transaction = transactions.find { it.id.toString() == transactionId }



    val categoryIcon =
        transaction?.let { getIconAndColor(it.category) } // Get icon and color for category

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(mintCream),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        XTopBar(
            text = "Transaction Details",
            backClick = {navController.popBackStack()},
            textColor = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (transaction != null) {
            Text(text = transaction.category)
        }else{
            Text(text = "Not found")
        }

        // Icon in a circular shape at the top
        if (categoryIcon != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(categoryIcon.second.copy(alpha = 0.2f)) // Background color for the icon
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = categoryIcon.first),
                    contentDescription = transaction.category,
                    tint = categoryIcon.second,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Transaction amount and type (Income or Expense)
        if (transaction != null) {
            Text(
                text = formatAmount(transaction.amount, transaction.type.displayName),
                style = MaterialTheme.typography.headlineSmall,
                color = if (transaction.type == TransactionType.INCOME) Color.Green else Color.Red,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        // Transaction type (Income/Expense)
        if (transaction != null) {
            Text(
                text = transaction.type.displayName,
                style = MaterialTheme.typography.bodyLarge,
                color = if (transaction.type == TransactionType.INCOME) Color.Green else Color.Red,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        // "Transaction Details" heading
        Text(
            text = "Transaction Details",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Transaction Details Rows
        if (transaction != null) {
            TransactionDetailRow("Status", transaction.type.displayName)
            TransactionDetailRow("Category", transaction.category)
            TransactionDetailRow("Description", transaction.description)
            TransactionDetailRow("Date", transaction.date)
            TransactionDetailRow("Time", transaction.time)
        }


        Spacer(modifier = Modifier.height(16.dp))


    }
}

/**
 * Helper composable function to create a row of transaction details.
 *
 * @param label The label for the row (e.g., "Status").
 * @param value The value for the row (e.g., "Income").
 */
@Composable
fun TransactionDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}




//@Preview(showBackground = true)
//@Composable
//fun Preview() {
//    // Mock ViewModel to simulate data for the preview.
//    val mockViewModel = TransactionViewModel(LocalContext.current)
//    // Add mock transaction data here if needed.
//
//    val navController = rememberNavController()
//
//    XpenseAppTheme {
//        // Displaying the AddScreen with mock data and a theme.
//        TransactionDetailsScreen(
//            transaction = TransactionData(
//                category = "Food",
//                description = "Eating",
//                amount  = 40.8,
//                time = "5 pm",
//                date = "11/12/1888",
//                type = TransactionType.INCOME),
//            navController = navController,
//        )
//    }
//}