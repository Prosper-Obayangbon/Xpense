package uk.ac.tees.mad.d3424757.xpenseapp.screens.transaction

import XTopBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionCategories.getIconAndColor
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionType
import uk.ac.tees.mad.d3424757.xpenseapp.utils.formatAmount
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.TransactionViewModel

/**
 * Composable function to display the details of a specific transaction.
 *
 * This screen shows detailed information about a transaction, including:
 * - Category with an icon and background color
 * - Transaction amount and type (Income or Expense)
 * - Transaction status, category, description, date, and time
 *
 * The data is fetched from a ViewModel which holds the list of transactions.
 * The transaction details are displayed in a column, with a top bar and various labels.
 *
 * @param modifier The modifier to be applied to the screen layout.
 * @param transactionId The unique ID of the transaction to display.
 * @param navController The NavController used to navigate back to the previous screen.
 * @param viewModel The ViewModel that contains the list of transactions.
 */
@Composable
fun TransactionDetailsScreen(
    modifier: Modifier = Modifier,
    transactionId: String,
    navController: NavController,
    viewModel: TransactionViewModel
) {
    // Collect transactions from the ViewModel
    val transactions by viewModel.transactions.collectAsState(emptyList())

    // Find the specific transaction based on transactionId
    val transaction = transactions.find { it.id.toString() == transactionId }

    // Get the category icon and color for the transaction
    val categoryIcon = transaction?.let { getIconAndColor(it.category) }

    // Main Column layout for the screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mintCream), // Background color
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        XTopBar(
            modifier = modifier,
            text = Constants.TRANSACTION_DETAILS_TITLE,  // Title of the screen
            backClick = { navController.popBackStack() }, // Back button action
            textColor = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Display category name or "Not Found" if transaction is null
        Text(text = transaction?.category ?: Constants.TRANSACTION_NOT_FOUND)

        // Show category icon if it exists
        categoryIcon?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(Constants.CIRCLE_ICON_SIZE.dp) // Size of the icon
                    .clip(CircleShape) // Circular shape
                    .background(it.second.copy(alpha = 0.2f)) // Background color with alpha
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = it.first), // Icon for the category
                    contentDescription = transaction?.category,
                    tint = it.second, // Tint color
                    modifier = Modifier.fillMaxSize() // Fill available space
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Transaction amount and type (Income or Expense)
        transaction?.let {
            Text(
                text = formatAmount(it.amount, it.type.displayName), // Format amount
                style = MaterialTheme.typography.headlineSmall,
                color = if (it.type == TransactionType.INCOME) Color.Green else Color.Red,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = it.type.displayName,  // Type of transaction (Income/Expense)
                style = MaterialTheme.typography.bodyLarge,
                color = if (it.type == TransactionType.INCOME) Color.Green else Color.Red,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        // Heading for "Transaction Details"
        Text(
            text = Constants.TRANSACTION_DETAILS_TITLE,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display transaction details (Status, Category, Description, Date, Time)
        transaction?.let {
            TransactionDetailRow(Constants.TRANSACTION_STATUS_LABEL, it.type.displayName)
            TransactionDetailRow(Constants.TRANSACTION_CATEGORY_LABEL, it.category)
            TransactionDetailRow(Constants.TRANSACTION_DESCRIPTION_LABEL, it.description)
            TransactionDetailRow(Constants.TRANSACTION_DATE_LABEL, it.date)
            TransactionDetailRow(Constants.TRANSACTION_TIME_LABEL, it.time)
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
            text = label, // Label for the row
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value, // Value for the row
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
