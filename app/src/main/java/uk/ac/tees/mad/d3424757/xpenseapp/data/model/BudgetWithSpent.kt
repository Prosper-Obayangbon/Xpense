package uk.ac.tees.mad.d3424757.xpenseapp.data.model

data class BudgetWithSpent(
    val id: Int,
    val category: String,
    val amount: Double, // The total budget amount
    val spent: Double, // The total spent for that category in the selected month
    val remainingAmount: Double // The remaining amount after spending
)