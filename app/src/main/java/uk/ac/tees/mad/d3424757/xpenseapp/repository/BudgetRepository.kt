package uk.ac.tees.mad.d3424757.xpenseapp.repository

import uk.ac.tees.mad.d3424757.xpenseapp.data.dao.TransactionDao
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetData
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetWithSpent
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData

/**
 * Repository class for managing budget-related data.
 * Acts as a mediator between the data layer (Room database) and the ViewModel.
 *
 * @param dao XpenseDatabase instance to access DAO methods
 */
class BudgetRepository(
    private val dao: XpenseDatabase
) {

    /**
     * Fetches all budgets for a given month.
     *
     * @param month The month in "yyyy-MM" format.
     * @return A list of BudgetData objects.
     */
    suspend fun getBudgetsForMonth(month: String): List<BudgetData> {
        // Fetch all budgets for the specified month using the DAO method
        return dao.budgetDao().getBudgetsForMonth(month)
    }

    /**
     * Fetches a specific budget by its ID.
     *
     * @param budgetId The ID of the budget.
     * @return The BudgetData object corresponding to the specified ID.
     */
    suspend fun getBudgetById(budgetId: Int): BudgetData {
        // Fetch a budget by ID using the DAO method
        return dao.budgetDao().getBudgetById(budgetId)
    }

    /**
     * Inserts a new budget into the database.
     *
     * @param budget The BudgetData object to be inserted.
     */
    suspend fun insertBudget(budget: BudgetData) {
        // Insert the new budget into the database using the DAO method
        dao.budgetDao().insertBudget(budget)
    }

    /**
     * Deletes a budget by its ID.
     *
     * @param budgetId The ID of the budget to delete.
     */
    suspend fun deleteBudget(budgetId: Int) {
        // Delete the budget from the database using the DAO method
        dao.budgetDao().deleteBudget(budgetId)
    }

    /**
     * Updates an existing budget in the database.
     *
     * @param budget The BudgetData object with updated information.
     */
    suspend fun updateBudget(budget: BudgetData) {
        // Update the budget in the database using the DAO method
        dao.budgetDao().updateBudget(budget)
    }
}
