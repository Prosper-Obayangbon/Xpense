package uk.ac.tees.mad.d3424757.xpenseapp.repository

import uk.ac.tees.mad.d3424757.xpenseapp.data.dao.TransactionDao
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetData
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetWithSpent
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData


class BudgetRepository(
    private val dao: XpenseDatabase
) {
    suspend fun getBudgetsForMonth(month: String): List<BudgetData> {
        // Fetch all budgets for the given month
        return dao.budgetDao().getBudgetsForMonth(month)

    }


    suspend fun insertBudget(budget: BudgetData) {
        dao.budgetDao().insertBudget(budget)
    }

    suspend fun deleteBudget(budgetId : Int){
        dao.budgetDao().deleteBudget(budgetId)
    }
}



