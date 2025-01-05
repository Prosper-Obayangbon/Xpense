package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import uk.ac.tees.mad.d3424757.xpenseapp.repository.TransactionRepository
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Category
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.DATE_FORMATTER
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * ViewModel for managing and processing transactions related to statistics.
 */
class StatsViewModel(
    context: Context
) : ViewModel() {
    private val repository: TransactionRepository
    val transactions: Flow<List<TransactionData>>

    // Initialize the repository with the DAO from the database
    init {
        val dao = XpenseDatabase.getDatabase(context)
        repository = TransactionRepository(dao)
        transactions= repository.loadTransactions()
    }


    /**
     * Maps a list of transactions to entries for a graph (e.g., line chart).
     * Converts the transaction date to milliseconds and creates `Entry` objects.
     *
     * @param transactions The list of transactions to map.
     * @return A sorted list of `Entry` objects for charting.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapTransactionsToEntries(transactions: List<TransactionData>): List<Entry> {
        return transactions
            .mapIndexed { index, transaction ->
                createEntryForTransaction(index, transaction)
            }
            .sortedBy { it.x }
    }

    /**
     * Filters transactions by type (e.g., "Expense" or "Income").
     *
     * @param transactions The list of transactions to filter.
     * @param type The type to filter by (e.g., "Expense" or "Income").
     * @return A filtered list of transactions that match the given type.
     */
    fun filterTransactionsByType(
        transactions: List<TransactionData>,
        type: String
    ): List<TransactionData> {
        return transactions.filter { it.type.displayName.equals(type, ignoreCase = true) }
    }

    /**
     * Generates a map of category names to their corresponding colors.
     * This is useful for visualizing categories with specific colors in a chart.
     *
     * @param categories The list of categories to map.
     * @return A map where the keys are category names and the values are their associated colors.
     */
    fun generateCategoryColorMap(categories: List<Category>): Map<String, Color> {
        return categories.associate { it.name to it.color }
    }

    /**
     * Maps transactions to pie chart entries based on their category.
     * It groups the transactions by category and sums the amounts for each category.
     *
     * @param transactions The list of transactions to map.
     * @param colorMap A map of categories to their colors.
     * @return A map of category names to the total amount for that category.
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
     * Calculates the total amount per category and the overall total for a specific transaction type.
     * This can be used to display category progress (e.g., total income or total expenses).
     *
     * @param type The type of transaction to calculate for (e.g., "Expense" or "Income").
     * @param transactions The list of transactions to process.
     * @return A pair containing a list of category totals and the overall total amount for the specified type.
     */
    fun calculateCategoryProgress(
        type: String,
        transactions: List<TransactionData>
    ): Pair<List<Pair<String, Double>>, Double> {
        val filteredTransactions = filterTransactionsByType(transactions, type)
        val categoryTotals = calculateCategoryTotals(filteredTransactions)

        val totalAmount = categoryTotals.values.sum()
        val categoryList = categoryTotals.map { it.key to it.value }

        return categoryList to totalAmount
    }

    /**
     * Converts a transaction to an Entry object for graph plotting.
     *
     * @param index The index of the transaction in the list.
     * @param transaction The transaction data to convert.
     * @return An Entry object with transaction amount and date in milliseconds.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createEntryForTransaction(index: Int, transaction: TransactionData): Entry {
        val dateMillis = parseDateToMillis(transaction.date)
        return Entry(index.toFloat(), transaction.amount.toFloat(), dateMillis)
    }

    /**
     * Parses a date string to milliseconds since epoch.
     *
     * @param date The date string in the format defined by DATE_FORMAT.
     * @return The date in milliseconds since epoch.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDateToMillis(date: String): Long {
        return runCatching {
            LocalDate.parse(date, DATE_FORMATTER).toEpochDay() * 86400000 // Convert to millis
        }.getOrDefault(0L)
    }

    /**
     * Calculates total amounts for each category.
     *
     * @param transactions The list of transactions to process.
     * @return A map of category names to their total amounts.
     */
    private fun calculateCategoryTotals(transactions: List<TransactionData>): Map<String, Double> {
        return transactions
            .groupBy { it.category }
            .mapValues { (_, transactions) -> transactions.sumOf { it.amount } }
    }
}
