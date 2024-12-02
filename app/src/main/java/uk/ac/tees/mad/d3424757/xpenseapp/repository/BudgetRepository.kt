package uk.ac.tees.mad.d3424757.xpenseapp.repository

import uk.ac.tees.mad.d3424757.xpenseapp.data.dao.TransactionDao
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetWithSpent


class BudgetRepository(
    private val dao: XpenseDatabase
) {
    suspend fun getBudgetsForMonth(month: String): List<BudgetWithSpent> {
        // Fetch all budgets for the given month
        val budgets = dao.budgetDao().getBudgetsForMonth(month)

        // For each budget, calculate the total spent and remaining amount
        return budgets.map { budget ->
            // Calculate the total spent for this category in the selected month
            val totalSpent = dao.TransactionDao().getTotalSpentForCategoryAndMonth(budget.category, month)

            // Calculate the remaining amount
            val remainingAmount = budget.amount - totalSpent
            // Return the budget with spent and remainingAmount
            BudgetWithSpent(
                id = budget.id,
                category = budget.category,
                amount = budget.amount,
                spent = totalSpent,
                remainingAmount = remainingAmount
            )
            }
        }
}



