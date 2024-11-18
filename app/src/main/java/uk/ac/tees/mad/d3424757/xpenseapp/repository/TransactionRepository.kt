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
        return dao.appDao().getTransactions()
    }

    /**
     * Inserts a new transaction into the database.
     *
     * @param transaction The transaction object to be inserted.
     */
    suspend fun insertTransaction(transaction: TransactionData) {
        dao.appDao().insertTransaction(transaction)
    }
}
