package uk.ac.tees.mad.d3424757.xpenseapp.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a budget in the application.
 * This class will be stored in the "budgets" table in the database.
 *
 * @param id The unique identifier for the budget (auto-generated).
 * @param category The category of the budget (e.g., "Food", "Transport").
 * @param amount The total budgeted amount for the category.
 * @param date The date associated with the budget (could be formatted as "yyyy-MM").
 * @param alertEnabled Indicates whether an alert is enabled for this budget.
 * @param alertThreshold The threshold for triggering an alert (e.g., 80% of the total budget).
 */
@Entity(tableName = "budgets")
data class BudgetData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Auto-generated unique ID
    val category: String,                        // The category of the budget
    val amount: Double,                          // The total amount allocated for this budget
    val date: String,                            // The date associated with this budget (e.g., "2024-12")
    val alertEnabled: Boolean,                   // Whether the alert is enabled for this budget
    val alertThreshold: Int                      // The threshold percentage for triggering an alert
)
