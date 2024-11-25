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
 */
@Composable
fun RecentTransactions(transactions : List<TransactionData>) {

        // Display the transactions in a lazy column
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(transactions.reversed()) { transaction ->
            val image = getIconAndColor(categoryName = transaction.category)
            if (image != null) {
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
