package uk.ac.tees.mad.d3424757.xpenseapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.formatter.ValueFormatter
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

/**
 * Formats the transaction amount based on the transaction type.
 * @param amount The transaction amount to format.
 * @param type The type of the transaction, either "INCOME" or "EXPENSE".
 * @return A string representing the formatted amount with the appropriate currency symbol.
 */
fun formatAmount(amount: Double, type: String): String {
    return if (type == "Income") {
        // If the transaction is income, format it as a positive value with a currency symbol.
        "£ %.2f".format(amount)
    } else {
        // If the transaction is an expense, format it as a negative value with a currency symbol.
        "- £%.2f".format(amount)
    }
}

/**
 * Groups transactions by date into different categories such as "Today", "Yesterday", "Last Week", etc.
 * @param transactions The list of transactions to group.
 * @return A map where the keys are date group labels (e.g., "TODAY", "LAST WEEK") and the values are lists of transactions in each group.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun groupTransactionsByDate(transactions: List<TransactionData>): Map<String, List<TransactionData>> {
    // Date format pattern for parsing transaction dates.
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    // Current date and calculated dates for categorizing the transactions.
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val lastWeekStart = today.minusWeeks(1)
    val lastMonthStart = today.minusMonths(1)

    // Helper function to parse a date string into a LocalDate object.
    fun parseDate(dateString: String): LocalDate? {
        return try {
            // Attempt to parse the date string to a LocalDate object
            LocalDate.parse(dateString, formatter)
        } catch (e: DateTimeParseException) {
            null // Return null if parsing fails
        }
    }

    // Map to store the grouped transactions by date.
    val groupedTransactions = mutableMapOf<String, List<TransactionData>>()

    // Iterate through each transaction and group them by the respective dates.
    val transactionGroups = mapOf(
        "TODAY" to today,
        "YESTERDAY" to yesterday,
        "LAST FEW DAYS" to lastWeekStart,
        "LAST WEEK" to lastWeekStart,
        "2 WEEKS AGO" to lastWeekStart.minusWeeks(1),
        "3 WEEKS AGO" to lastWeekStart.minusWeeks(2),
        "LAST MONTH" to lastMonthStart,
        "OLDER" to lastMonthStart.minusMonths(1)
    )

    // Iterate through each group and filter transactions accordingly
    transactionGroups.forEach { (groupName, dateThreshold) ->
        val filteredTransactions = transactions.filter {
            val transactionDate = parseDate(it.date)
            when (groupName) {
                "TODAY" -> transactionDate?.isEqual(today) == true
                "YESTERDAY" -> transactionDate?.isEqual(yesterday) == true
                "LAST FEW DAYS" -> transactionDate != null && transactionDate.isBefore(yesterday) && transactionDate.isAfter(lastWeekStart)
                "LAST WEEK" -> transactionDate != null && !transactionDate.isBefore(lastWeekStart) && transactionDate.isBefore(today)
                "2 WEEKS AGO" -> transactionDate != null && transactionDate.isBefore(lastWeekStart) && transactionDate.isAfter(lastWeekStart.minusWeeks(1))
                "3 WEEKS AGO" -> transactionDate != null && transactionDate.isBefore(lastWeekStart.minusWeeks(1)) && transactionDate.isAfter(lastWeekStart.minusWeeks(2))
                "LAST MONTH" -> transactionDate != null && !transactionDate.isBefore(lastMonthStart) && transactionDate.isBefore(lastMonthStart.minusMonths(1))
                "OLDER" -> transactionDate != null && transactionDate.isBefore(lastMonthStart.minusMonths(1))
                else -> false
            }
        }

        if (filteredTransactions.isNotEmpty()) {
            groupedTransactions[groupName] = filteredTransactions
        }
    }

    // Return the grouped transactions, filtering out empty groups.
    return groupedTransactions
}

/**
 * Gets the current month in "MM/yyyy" format.
 * @return The current month and year in "MM/yyyy" format (e.g., "12/2024").
 */
fun getCurrentMonth(): String {
    val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
    return dateFormat.format(Date()) // Format the current date as "MM/yyyy".
}

/**
 * Gets the current date in "yyyy-MM-dd" format.
 * @return The current date in "yyyy-MM-dd" format (e.g., "2024-12-25").
 */
fun getCurrentDate(): String {
    val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return formatter.format(Date()) // Format the current date as "yyyy-MM-dd".
}


