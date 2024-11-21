package uk.ac.tees.mad.d3424757.xpenseapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
object TransactionFilters {

    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Helper to parse String to LocalDate
    private fun parseDate(dateString: String): LocalDate? {
        return try {
            LocalDate.parse(dateString, formatter)
        } catch (e: DateTimeParseException) {
            null
        }
    }


    fun filterByMonth(transactions: List<TransactionData>, monthIndex: Int): Map<String, List<TransactionData>> {
        val filteredMap = mutableMapOf<String, List<TransactionData>>()

        transactions.filter { transaction ->
            val transactionDate = parseDate(transaction.date)
            val transactionMonth = transactionDate?.monthValue
            transactionMonth == monthIndex // Compare the month of each transaction to the selected month
        }.forEach { transaction ->
            val transactionDate = parseDate(transaction.date)
            val monthName = transactionDate?.month?.name ?: "Unknown" // Get the month name

            // Group transactions by month name
            val currentList = filteredMap[monthName] ?: emptyList()
            filteredMap[monthName] = currentList + transaction
        }

        return filteredMap
    }



    fun filterByType(
        transactions: Map<String, List<TransactionData>>,
        type: TransactionType
    ): Map<String, List<TransactionData>> {
        val filteredMap = mutableMapOf<String, List<TransactionData>>()

        transactions.forEach { (category, transactionList) ->
            // Filter transactions by the selected type
            filteredMap[category] = transactionList.filter { transaction ->
                transaction.type == type // Assuming `TransactionData` has a `type` property
            }
        }

        return filteredMap
    }

    // Filter by both month and transaction type
    fun filterByMonthAndType(
        transactions: List<TransactionData>,
        month: Int,
        type: TransactionType
    ): List<TransactionData> {
        return transactions.filter {
            val date = parseDate(it.date)
            date != null && date.monthValue == month && it.type == type
        }
    }
}
