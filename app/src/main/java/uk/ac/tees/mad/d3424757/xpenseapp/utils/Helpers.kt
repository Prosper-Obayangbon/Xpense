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
 * Formats the amount based on the transaction type (INCOME/EXPENSE).
 * @param amount The amount to be formatted.
 * @param type The type of transaction, either "INCOME" or "EXPENSE".
 * @return A string representing the formatted amount with the currency symbol.
 */
fun formatAmount(amount: Double, type: String): String {
    return if (type == "INCOME") {
        // If income, format as positive value
        "£ $amount"
    } else {
        // If expense, format as negative value
        "- £$amount"
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun groupTransactionsByDate(transactions: List<TransactionData>): Map<String, List<TransactionData>> {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val lastWeekStart = today.minusWeeks(1)
    val lastMonthStart = today.minusMonths(1)

    // Helper to parse String to LocalDate
    fun parseDate(dateString: String): LocalDate? {
        return try {
            LocalDate.parse(dateString, formatter)
        } catch (e: DateTimeParseException) {
            null // Return null if parsing fails
        }
    }

    val remainingTransactions = transactions.toMutableList()
    val groupedTransactions = mutableMapOf<String, List<TransactionData>>()

    // Group "Today"
    groupedTransactions["TODAY"] = remainingTransactions.filter {
        parseDate(it.date)?.isEqual(today) == true
    }.also { remainingTransactions.removeAll(it) }

    // Group "Yesterday"
    groupedTransactions["YESTERDAY"] = remainingTransactions.filter {
        parseDate(it.date)?.isEqual(yesterday) == true
    }.also { remainingTransactions.removeAll(it) }

    // Group "Last Few Days" (2–6 days ago, between Yesterday and Last Week)
    groupedTransactions["LAST FEW DAYS"] = remainingTransactions.filter {
        val date = parseDate(it.date)
        date != null && date.isBefore(yesterday) && date.isAfter(lastWeekStart)
    }.also { remainingTransactions.removeAll(it) }

    // Group "Last Week" (7–13 days ago)
    groupedTransactions["LAST WEEK"] = remainingTransactions.filter {
        val date = parseDate(it.date)
        date != null && !date.isBefore(lastWeekStart) && date.isBefore(today)
    }.also { remainingTransactions.removeAll(it) }
    // Group "Last 2 Weeks" (14–20 days ago)
    groupedTransactions["2 WEEKS AGO"] = remainingTransactions.filter {
        val date = parseDate(it.date)
        date != null && date.isBefore(lastWeekStart) && date.isAfter(lastWeekStart.minusWeeks(1))
    }.also { remainingTransactions.removeAll(it) }

    // Group "Last 3 Weeks" (21–27 days ago)
    groupedTransactions["3 WEEKS AGO"] = remainingTransactions.filter {
        val date = parseDate(it.date)
        date != null && date.isBefore(lastWeekStart.minusWeeks(1)) && date.isAfter(lastWeekStart.minusWeeks(2))
    }.also { remainingTransactions.removeAll(it) }

    // Group "Last Month" (28 days ago up to one month)
    groupedTransactions["LAST MONTH"] = remainingTransactions.filter {
        val date = parseDate(it.date)
        date != null && !date.isBefore(lastMonthStart) && date.isBefore(lastMonthStart.minusMonths(1))
    }.also { remainingTransactions.removeAll(it) }

    // Group "Older" (More than a month ago)
    groupedTransactions["OLDER"] = remainingTransactions.filter {
        val date = parseDate(it.date)
        date != null && date.isBefore(lastMonthStart.minusMonths(1))
    }.also { remainingTransactions.removeAll(it) }

    // Filter out empty groups
    return groupedTransactions.filterValues { it.isNotEmpty() }
}




@RequiresApi(Build.VERSION_CODES.O)
fun String.toMonthName(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
    return try {
        val date = LocalDate.parse(this, formatter)
        date.month.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
    } catch (e: Exception) {
        "Invalid Date" // Handle invalid date formats
    }
}






