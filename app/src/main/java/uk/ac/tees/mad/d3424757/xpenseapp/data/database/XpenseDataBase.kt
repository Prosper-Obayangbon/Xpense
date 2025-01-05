package uk.ac.tees.mad.d3424757.xpenseapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.dao.BudgetDao
import uk.ac.tees.mad.d3424757.xpenseapp.data.dao.TransactionDao
import uk.ac.tees.mad.d3424757.xpenseapp.data.dao.UserProfileDao
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetData
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.UserProfile
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.DATABASE_NAME

/**
 * The Room Database for the Xpense application.
 * This class serves as the main access point for interacting with the app's database.
 *
 * Entities:
 * - TransactionData: Stores the user's transactions.
 * - BudgetData: Stores the user's budget information.
 * - UserProfile: Stores the user's profile information.
 *
 * Version: 1 (Update version and add migrations when modifying schema)
 */
@Database(entities = [TransactionData::class, BudgetData::class, UserProfile::class], version = 1)
abstract class XpenseDatabase : RoomDatabase() {

    // Abstract methods to get DAO instances
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: XpenseDatabase? = null

        /**
         * Gets or creates a database instance for the specified user.
         *
         * @param context The application context.
         * @param userId The unique identifier for the user.
         * @return The XpenseDatabase instance for the user.
         */
        fun getDatabase(context: Context): XpenseDatabase {
            // Use userId to differentiate database files
            val databaseName = DATABASE_NAME

            // Check if an instance exists for the current user
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    XpenseDatabase::class.java,
                    databaseName
                ).build()

                // Set the singleton instance
                INSTANCE = instance
                instance
            }
        }

        /**
         * Clears the current database instance (useful when switching users).
         */
        fun clearInstance() {
            INSTANCE = null
        }
    }

}
