package uk.ac.tees.mad.d3424757.xpenseapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionType

/**
 * Data class representing a transaction in the system.
 * This entity will be stored in the "Transactions" table of the database.
 *
 * @param id Unique identifier for the transaction (auto-generated).
 * @param category The category of the transaction (e.g., "Food", "Transport").
 * @param description A brief description of the transaction (e.g., "Grocery shopping").
 * @param amount The amount of the transaction (positive for income, negative for expenses).
 * @param time The time of the transaction (e.g., "12:30 PM").
 * @param date The date of the transaction (e.g., "2024-12-25").
 * @param type The type of the transaction (either INCOME or EXPENSE).
 */
@Entity(tableName = "Transactions")
data class TransactionData(
    @PrimaryKey(autoGenerate = true) val id : Long = 0,           // Unique ID for each transaction
    val category : String,                                        // Category of the transaction (e.g., "Food")
    val description: String,                                      // Description of the transaction (e.g., "Grocery shopping")
    val amount: Double,                                           // Amount for the transaction (positive for income, negative for expense)
    val time: String,                                             // Time of the transaction (e.g., "12:30 PM")
    val date : String,                                            // Date of the transaction (e.g., "2024-12-25")
    val type : TransactionType                                    // Type of transaction (INCOME or EXPENSE)
)
