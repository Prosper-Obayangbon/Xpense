package uk.ac.tees.mad.d3424757.xpenseapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetData

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budgets WHERE strftime('%Y-%m', date / 1000, 'unixepoch') = :monthYear ORDER BY date ASC")
    suspend fun getBudgetsForMonth(monthYear: String): List<BudgetData>
}