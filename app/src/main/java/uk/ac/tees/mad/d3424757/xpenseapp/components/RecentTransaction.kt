package uk.ac.tees.mad.d3424757.xpenseapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.utils.formatAmount
import uk.ac.tees.mad.d3424757.xpenseapp.utils.getIconAndColor

/**
 * Section displaying the list of recent transactions.
 */
@Composable
fun RecentTransactions(transactions : List<TransactionData>) {

        // Display the transactions in a lazy column
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(transactions) { transaction ->
            val image = getIconAndColor(category = transaction.category.displayName)
            TransactionItem(
                itemName = transaction.name,
                description = transaction.description,
                amount = formatAmount(transaction.amount, transaction.category.displayName),
                time = transaction.time.toString(),
                icon = image.first,
                iconColor = image.second
            )
        }
    }
}
