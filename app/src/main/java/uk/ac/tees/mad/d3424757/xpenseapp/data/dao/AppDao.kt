package uk.ac.tees.mad.d3424757.xpenseapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: TransactionData): Long

    @Delete
    suspend fun deleteTransaction(transaction: TransactionData)


    @Query("SELECT * FROM Transactions")
    fun getTransactions(): Flow<List<TransactionData>>




}