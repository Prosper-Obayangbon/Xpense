package uk.ac.tees.mad.d3424757.xpenseapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
object TransactionFilters {

    // DateTimeFormatter to parse the date in the "dd/MM/yyyy" format
    private val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    // Helper function to safely parse a String date into a LocalDate object.
    // Returns null if the date format is invalid.
    private fun parseDate(dateString: String): LocalDate? {
        return try {
            // Attempt to parse the date using the predefined formatter
            LocalDate.parse(dateString, formatter)
        } catch (e: DateTimeParseException) {
            // If the date format is invalid, return null
            null
        }
    }

    /**
     * Filters the transactions by the selected month and groups them by month name.
     *
     * @param transactions List of transactions to be filtered.
     * @param monthIndex The month index (1-12) to filter by.
     * @return A map where the key is the month name and the value is a list of transactions for that month.
     */
    fun filterByMonth(transactions: List<TransactionData>, monthIndex: String): Map<String, List<TransactionData>> {
        val filteredMap = mutableMapOf<String, List<TransactionData>>()

        // Filter transactions by comparing their month value to the provided monthIndex
        transactions.filter { transaction ->
            val transactionDate = parseDate(transaction.date)
            val transactionMonth = transactionDate?.monthValue?.toString()?.padStart(2, '0')
            transactionMonth == monthIndex // Compare the month of each transaction to the selected month
        }.forEach { transaction ->
            // Get the month name of the transaction
            val transactionDate = parseDate(transaction.date)
            val monthName = transactionDate?.month?.name ?: "Unknown" // Default to "Unknown" if the month is null

            // Add the transaction to the corresponding month in the filtered map
            val currentList = filteredMap[monthName] ?: emptyList()
            filteredMap[monthName] = currentList + transaction
        }

        return filteredMap
    }

    /**
     * Filters the transactions by the selected type (e.g., EXPENSE or INCOME).
     *
     * @param transactions A map of transactions grouped by category.
     * @param type The transaction type to filter by (e.g., TransactionType.EXPENSE).
     * @return A map where the key is the category name and the value is a list of transactions of the selected type.
     */
    fun filterByType(
        transactions: Map<String, List<TransactionData>>,
        type: TransactionType
    ): Map<String, List<TransactionData>> {
        val filteredMap = mutableMapOf<String, List<TransactionData>>()

        // Iterate through each category and filter the transactions by type
        transactions.forEach { (category, transactionList) ->
            // Filter the list of transactions in each category based on the selected type
            filteredMap[category] = transactionList.filter { transaction ->
                transaction.type == type // Compare the transaction type to the selected type
            }
        }

        return filteredMap
    }

    /**
     * Filters the transactions by both the selected month and type.
     *
     * @param transactions The list of all transactions to filter.
     * @param month The month (1-12) to filter by.
     * @param type The transaction type (e.g., EXPENSE or INCOME) to filter by.
     * @return A list of transactions that match both the selected month and type.
     */
    fun filterByMonthAndType(
        transactions: List<TransactionData>,
        month: Int,
        type: TransactionType
    ): List<TransactionData> {
        // Filter transactions that match both the month and type
        return transactions.filter {
            val date = parseDate(it.date)
            date != null && date.monthValue == month && it.type == type // Match month and type
        }
    }

    fun String.toMonthName(): String {
        // Define the expected format
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

        // Parse the string using the defined format
        val date = LocalDate.parse(this, dateFormatter)

        // Format the date to get the month name
        val monthFormatter = DateTimeFormatter.ofPattern("MMMM")
        return date.format(monthFormatter)
    }


}
