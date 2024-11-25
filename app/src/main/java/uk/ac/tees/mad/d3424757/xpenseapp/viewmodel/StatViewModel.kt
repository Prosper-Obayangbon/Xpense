package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel

import android.content.Context
import android.os.Build
import android.util.Half.toFloat
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.utils.Utils
import uk.ac.tees.mad.d3424757.xpenseapp.data.dao.TransactionDao
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import uk.ac.tees.mad.d3424757.xpenseapp.repository.TransactionRepository
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Category
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionType
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject


class StatsViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    val transactions = repository.loadTransactions()

    /**
     * Maps a list of transactions to entries for a graph (e.g., line chart).
     * @param transactions The list of transactions to map.
     * @return A sorted list of `Entry` objects.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapTransactionsToEntries(transactions: List<TransactionData>): List<Entry> {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
        return transactions.mapIndexed { index, transaction ->
            val dateMillis = runCatching {
                LocalDate.parse(transaction.date, formatter).toEpochDay() * 86400000 // Convert to millis
            }.getOrDefault(0L)
            Entry(index.toFloat(), transaction.amount.toFloat(), dateMillis)
        }.sortedBy { it.x }
    }

    /**
     * Filters transactions by type (e.g., "Expense" or "Income").
     * @param transactions The list of transactions to filter.
     * @param type The type to filter by.
     * @return A filtered list of transactions.
     */
    fun filterTransactionsByType(
        transactions: List<TransactionData>,
        type: String
    ): List<TransactionData> {
        return transactions.filter { it.type.displayName.equals(type, ignoreCase = true) }
    }

    /**
     * Generates a map of category names to their corresponding colors.
     * @param categories The list of categories.
     * @return A map of category names to colors.
     */
    fun generateCategoryColorMap(categories: List<Category>): Map<String, Color> {
        return categories.associate { it.name to it.color }
    }

    /**
     * Maps transactions to pie chart entries based on their category.
     * @param transactions The list of transactions.
     * @param colorMap A map of categories to their colors.
     * @return A map of category names to total amounts.
     */
    fun mapToPieEntries(
        transactions: List<TransactionData>,
        colorMap: Map<String, Color>
    ): Map<String, Float> {
        return transactions
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount }.toFloat() }
            .filterKeys { colorMap.containsKey(it) }
    }

    /**
     * Calculates the total amount per category and the overall total for a specific type.
     * @param type The type of transaction to calculate for.
     * @param transactions The list of transactions.
     * @return A pair containing a list of category totals and the overall total amount.
     */
    fun calculateCategoryProgress(
        type: String,
        transactions: List<TransactionData>
    ): Pair<List<Pair<String, Double>>, Double> {
        val filteredTransactions = transactions.filter { it.type.displayName == type }
        val categoryTotals = filteredTransactions
            .groupBy { it.category }
            .mapValues { (_, transactions) -> transactions.sumOf { it.amount } }

        val totalAmount = categoryTotals.values.sum()
        val categoryList = categoryTotals.map { it.key to it.value }

        return categoryList to totalAmount
    }
}

