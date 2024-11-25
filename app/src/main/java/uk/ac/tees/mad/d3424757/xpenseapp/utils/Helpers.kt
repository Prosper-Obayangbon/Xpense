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
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy") // Example format: "2023-11-18"
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val beforeYesterday = today.minusDays(2)
    val lastWeekStart = today.minusWeeks(1)
    val lastMonth = today.minusMonths(1)

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
    groupedTransactions["Today"] = transactions.filter {
        val date = parseDate(it.date)
        date != null && date.isEqual(today) // Only match transactions from today
    }

    // Group by "Yesterday"
    groupedTransactions["Yesterday"] = transactions.filter {
        val date = parseDate(it.date)
        date != null && date.isEqual(yesterday) // Only match transactions exactly from yesterday
    }

    // Group by "Before Yesterday"
    groupedTransactions["Before Yesterday"] = transactions.filter {
        val date = parseDate(it.date)
        date != null && date.isBefore(yesterday) && !date.isEqual(today) && !date.isEqual(yesterday)
    }

    // Group by "Last Week"
    groupedTransactions["Last Week"] = transactions.filter {
        val date = parseDate(it.date)
        date != null && date.isAfter(lastWeekStart) && date.isBefore(today) && !date.isEqual(yesterday)
    }

    // Group by "Last Month"
    groupedTransactions["Last Month"] = transactions.filter {
        val date = parseDate(it.date)
        date != null && date.isAfter(lastMonth) && date.isBefore(lastWeekStart)
    }

    // Group by "Older"
    groupedTransactions["Older"] = transactions.filter {
        val date = parseDate(it.date)
        date != null && date.isBefore(lastMonth)
    }

    // Only add "Days of the Week" if they are not in "Today", "Yesterday", "Last Week", or "Last Month"
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    daysOfWeek.forEach { dayOfWeek ->
        // Only add days of the week if they are not already included in "Today", "Yesterday", "Last Week", or "Last Month"
        val filteredTransactions = transactions.filter {
            val date = parseDate(it.date)
            date != null && !date.isEqual(today) && !date.isEqual(yesterday) && !date.isAfter(lastWeekStart) && date.dayOfWeek.name == dayOfWeek.toUpperCase()
        }
        if (filteredTransactions.isNotEmpty()) {
            groupedTransactions[dayOfWeek] = filteredTransactions
        }
    }

    // Filter out empty categories
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





