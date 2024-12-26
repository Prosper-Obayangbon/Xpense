package uk.ac.tees.mad.d3424757.xpenseapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetData

@Dao
interface BudgetDao {

    /**
     * Get a list of budgets for a specific month-year.
     *
     * @param monthYear The month-year in the format "YYYY-MM".
     * @return A list of BudgetData for the specified month-year.
     */
    @Query("SELECT * FROM budgets WHERE substr(date, 1, 7) = :monthYear ORDER BY date ASC")
    suspend fun getBudgetsForMonth(monthYear: String): List<BudgetData>

    /**
     * Get a specific budget by its ID.
     *
     * @param id The ID of the budget.
     * @return The BudgetData object corresponding to the specified ID.
     */
    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: Int): BudgetData

    /**
     * Delete a specific budget by its ID.
     *
     * @param budgetId The ID of the budget to delete.
     */
    @Query("DELETE FROM budgets WHERE id = :budgetId")
    suspend fun deleteBudget(budgetId: Int)

    /**
     * Insert a new budget into the database.
     *
     * @param budget The BudgetData object to insert into the database.
     */
    @Insert
    suspend fun insertBudget(budget: BudgetData)

    /**
     * Update an existing budget in the database.
     *
     * @param budget The BudgetData object with updated values.
     */
    @Update
    suspend fun updateBudget(budget: BudgetData)
}
