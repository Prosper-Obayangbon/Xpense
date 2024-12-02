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
    @Insert
    suspend fun insertTransaction(transaction: TransactionData): Long

    @Delete
    suspend fun deleteTransaction(transaction: TransactionData)


    @Query("SELECT * FROM Transactions")
    fun getTransactions(): Flow<List<TransactionData>>

    // Query to get all expense transactions for a given category and month in "dd/mm/yyyy" format
    @Query("SELECT SUM(amount) FROM transactions WHERE category = :category AND type = 'Expense' AND substr(date, 4, 7) = :month")
    suspend fun getTotalSpentForCategoryAndMonth(category: String, month: String): Double











}