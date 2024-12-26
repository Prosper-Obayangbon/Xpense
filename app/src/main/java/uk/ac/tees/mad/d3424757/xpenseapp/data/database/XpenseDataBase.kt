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
        // Volatile ensures that the instance is updated immediately across all threads.
        @Volatile
        private var INSTANCE: XpenseDatabase? = null

        /**
         * Gets the singleton instance of the XpenseDatabase.
         * This method ensures only one instance of the database exists at any given time.
         *
         * @param context The application context for creating the database.
         * @return The XpenseDatabase instance.
         */
        fun getDatabase(context: Context): XpenseDatabase {
            // Check if the instance is already created
            return INSTANCE ?: synchronized(this) {
                // Create a new instance if it doesn't exist
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    XpenseDatabase::class.java,
                    DATABASE_NAME // Database name defined in Constants
                ).build()

                // Set the instance to the newly created one
                INSTANCE = instance
                instance
            }
        }
    }
}
