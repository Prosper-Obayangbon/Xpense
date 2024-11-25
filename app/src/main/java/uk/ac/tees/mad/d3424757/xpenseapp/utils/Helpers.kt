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

    val groupedTransactions = mutableMapOf<String, List<TransactionData>>()

    // Group by "Today"
    groupedTransactions["TODAY"] = transactions.filter {
        parseDate(it.date)?.isEqual(today) == true
    }

    // Group by "Yesterday"
    groupedTransactions["YESTERDAY"] = transactions.filter {
        parseDate(it.date)?.isEqual(yesterday) == true
    }

    // Group by specific days of the week for transactions between "Yesterday" and "Last Week"
    val daysBetween = transactions.filter {
        val date = parseDate(it.date)
        date != null && date.isBefore(yesterday) && date.isAfter(lastWeekStart)
    }
    daysBetween.forEach { transaction ->
        val date = parseDate(transaction.date)
        if (date != null) {
            val dayName =
                date.dayOfWeek.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } // Convert to "Monday", "Tuesday", etc.
            groupedTransactions[dayName] = groupedTransactions.getOrDefault(dayName, emptyList()) + transaction
        }
    }

    // Group transactions between "Last Week" and "Last Month" by weeks
    val transactionsBetweenWeeks = transactions.filter {
        val date = parseDate(it.date)
        date != null && date.isBefore(lastWeekStart) && date.isAfter(lastMonthStart)
    }

    transactionsBetweenWeeks.forEach { transaction ->
        val date = parseDate(transaction.date)
        if (date != null) {
            val weeksAgo = ((today.toEpochDay() - date.toEpochDay()) / 7).toInt() // Calculate weeks ago
            val weekLabel = when (weeksAgo) {
                2 -> "2 Weeks Ago"
                3 -> "3 Weeks Ago"
                4 -> "4 Weeks Ago"
                else -> "Older" // Fallback for safety
            }
            groupedTransactions[weekLabel] = groupedTransactions.getOrDefault(weekLabel, emptyList()) + transaction
        }
    }

    // Group by "Last Month"
    groupedTransactions["Last Month"] = transactions.filter {
        val date = parseDate(it.date)
        date != null && date.isBefore(lastMonthStart) && date.isAfter(lastMonthStart.minusMonths(1))
    }

    // Group by "Older"
    groupedTransactions["Older"] = transactions.filter {
        val date = parseDate(it.date)
        date != null && date.isBefore(lastMonthStart.minusMonths(1))
    }

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






