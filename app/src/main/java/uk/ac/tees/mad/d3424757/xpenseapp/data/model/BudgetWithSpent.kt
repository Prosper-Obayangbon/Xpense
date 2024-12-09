package uk.ac.tees.mad.d3424757.xpenseapp.data.model

data class BudgetWithSpent(
    val id: Int,
    val category: String,
    val amount: Double,
    val spent: Double,
    val remainingAmount: Double
)