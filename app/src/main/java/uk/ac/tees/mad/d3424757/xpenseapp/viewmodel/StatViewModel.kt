package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel

import android.content.Context
import android.util.Half.toFloat
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
import java.util.Locale
import javax.inject.Inject


class StatsViewModel(context : Context) : ViewModel() {


    private val dao = XpenseDatabase.getDatabase(context)
    private val repository = TransactionRepository(dao)

    val transactions = repository.loadTransactions()



    fun mapTransactionsToEntries(transactions: List<TransactionData>): List<Entry> {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return transactions.mapIndexed { index, transaction ->
            val dateMillis = dateFormat.parse(transaction.date)?.time ?: 0L
            Entry(index.toFloat(), transaction.amount.toFloat(), dateMillis)
        }.sortedBy { it.x } // Sort by the X-axis value
    }

    fun filterTransactionsByType(
        transactions: List<TransactionData>,
        type: String
    ): List<TransactionData> {
        return transactions.filter { it.type.displayName.equals(type, ignoreCase = true) }
    }

    fun generateCategoryColorMap(categories: List<Category>): Map<String, Color> {
        return categories.associate { it.name to it.color }
    }






    // Maps transactions to pie entries for the Donut Chart
    fun mapToPieEntries(
        transactions: List<TransactionData>,
        colorMap: Map<String, Color>
    ): Map<String, Float> {
        return transactions.groupBy { it.category }.mapValues { entry ->
            entry.value.sumOf { it.amount}.toFloat()
        }.filterKeys { colorMap.containsKey(it) } // Use only categories with defined colors
    }


    fun calculateCategoryProgress(type : String, transactions: List<TransactionData>): Pair<List<Pair<String, Double>>, Double> {
        val filteredTransactions = transactions.filter { it.type.displayName == type }
        val categoryTotals = filteredTransactions
            .groupBy { it.category }
            .mapValues { (_, transactions) -> transactions.sumOf { it.amount } }

        val totalAmount = categoryTotals.values.sum()
        val categoryList = categoryTotals.map { it.key to it.value }

        return categoryList to totalAmount
    }

}
