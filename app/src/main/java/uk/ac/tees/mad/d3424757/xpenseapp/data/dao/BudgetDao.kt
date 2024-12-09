package uk.ac.tees.mad.d3424757.xpenseapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetData

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budgets WHERE substr(date, 1, 7) = :monthYear ORDER BY date ASC")
    suspend fun getBudgetsForMonth(monthYear: String): List<BudgetData>

    @Query("DELETE FROM budgets WHERE id = :budgetId")
    suspend fun deleteBudget(budgetId: Int)


    @Insert
    suspend fun insertBudget(budget: BudgetData)
}