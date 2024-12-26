package uk.ac.tees.mad.d3424757.xpenseapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionCategories.getIconAndColor
import uk.ac.tees.mad.d3424757.xpenseapp.utils.formatAmount

/**
 * Section displaying the list of recent transactions.
 *
 * @param transactions The list of recent transactions to display.
 * This composable function is responsible for rendering the list of transactions.
 * It uses a LazyColumn to efficiently display potentially long lists of transactions.
 */
@Composable
fun RecentTransactions(transactions: List<TransactionData>) {

    // Display the transactions in a LazyColumn for optimal performance with large datasets
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Reverse the list to display the most recent transaction at the top
        items(transactions.reversed()) { transaction ->
            // Get the icon and color associated with the transaction's category
            val image = getIconAndColor(categoryName = transaction.category)

            // If an image and color were found for the category, display the transaction item
            if (image != null) {
                // Call the TransactionItem composable to display the details of each transaction
                TransactionItem(
                    itemName = transaction.category,
                    description = transaction.description,
                    amount = formatAmount(transaction.amount, transaction.type.displayName),
                    time = transaction.time,
                    icon = image.first,
                    iconColor = image.second
                )
            }
        }
    }
}
