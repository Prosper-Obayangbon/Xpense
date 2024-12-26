package uk.ac.tees.mad.d3424757.xpenseapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetData
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData

@Dao
interface TransactionDao {

    /**
     * Insert a new transaction into the database.
     *
     * @param transaction The TransactionData object to be inserted.
     * @return The row ID of the inserted transaction.
     */
    @Insert
    suspend fun insertTransaction(transaction: TransactionData): Long

    /**
     * Delete a specific transaction from the database.
     *
     * @param transaction The TransactionData object to be deleted.
     */
    @Delete
    suspend fun deleteTransaction(transaction: TransactionData)

    /**
     * Get all transactions from the database.
     * Returns a Flow to allow for live updates (reactive programming).
     *
     * @return A Flow of a list of TransactionData objects.
     */
    @Query("SELECT * FROM Transactions")
    fun getTransactions(): Flow<List<TransactionData>>

    /**
     * Get the total amount spent for a specific category and month (only expenses).
     *
     * @param category The category of the transaction (e.g., "Food", "Shopping").
     * @param monthYr The month-year in the format "YYYY-MM".
     * @return The total amount spent in the specified category and month.
     */
    @Query("SELECT SUM(amount) FROM transactions WHERE category = :category AND type = 'EXPENSE' AND substr(date, 1, 7) = :monthYr")
    suspend fun getTotalSpentForCategoryAndMonth(category: String, monthYr: String): Double
}
