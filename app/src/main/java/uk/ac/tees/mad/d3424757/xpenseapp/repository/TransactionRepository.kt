package uk.ac.tees.mad.d3424757.xpenseapp.repository


import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData

/**
 * Repository class to handle data operations related to transactions.
 * This class acts as an intermediary between the ViewModel and the DAO,
 * abstracting the data source access from the ViewModel.
 */
class TransactionRepository(private val dao: XpenseDatabase) {

    /**
     * Fetches all the transactions from the database.
     * This method exposes a flow of transaction data, which the ViewModel can observe.
     *
     * @return A Flow of a list of transactions.
     */
     fun loadTransactions(): Flow<List<TransactionData>> {
        return dao.transactionDao().getTransactions()
    }

    /**
     * Inserts a new transaction into the database.
     *
     * @param transaction The transaction object to be inserted.
     */
    suspend fun insertTransaction(transaction: TransactionData) {
        dao.transactionDao().insertTransaction(transaction)
    }

    /**
     * Suspended function to fetch the total amount spent for a specific category and month.
     *
     * @param category The category for which the total spent needs to be calculated (e.g., "Food", "Transport").
     * @param month The month in "yyyy-MM" format (e.g., "2024-12").
     * @return A Double value representing the total amount spent in the specified category for the given month.
     */
    suspend fun loadTotalSpent(category: String, month: String): Double {
        return dao.transactionDao().getTotalSpentForCategoryAndMonth(category, month)
    }



}
