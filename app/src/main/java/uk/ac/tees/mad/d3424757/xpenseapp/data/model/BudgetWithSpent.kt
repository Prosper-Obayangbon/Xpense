package uk.ac.tees.mad.d3424757.xpenseapp.data.model

/**
 * Data class representing a budget along with the amount spent and remaining balance.
 * This is typically used to show the current status of a budget in the application.
 *
 * @param id The unique identifier for the budget.
 * @param category The category of the budget (e.g., "Food", "Transport").
 * @param amount The total allocated budget for the category.
 * @param spent The total amount spent from the budget category.
 */
data class BudgetWithSpent(
    val id: Int,                // The unique ID for the budget
    val category: String,       // The category for the budget (e.g., "Food")
    val amount: Double,         // The total allocated amount for the budget
    val spent: Double           // The total amount spent from the budget
) {
    // Computed property to calculate the remaining amount
    val remainingAmount: Double
        get() = amount - spent   // Remaining amount = allocated amount - spent
}
