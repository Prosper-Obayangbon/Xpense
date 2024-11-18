package uk.ac.tees.mad.d3424757.xpenseapp.utils

    enum class TransactionType(val displayName: String) {
        INCOME("INCOME"),
        EXPENSE("EXPENSE")
    }

    // Helper function to determine if it's income
    fun TransactionType.isIncome() = this == TransactionType.INCOME
