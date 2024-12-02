package uk.ac.tees.mad.d3424757.xpenseapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.dao.BudgetDao
import uk.ac.tees.mad.d3424757.xpenseapp.data.dao.TransactionDao
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetData
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.DATABASE_NAME

@Database(entities = [TransactionData::class, BudgetData::class], version = 1)
abstract  class XpenseDatabase : RoomDatabase() {
    abstract fun TransactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: XpenseDatabase? = null

        fun getDatabase(context: Context): XpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    XpenseDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }


}