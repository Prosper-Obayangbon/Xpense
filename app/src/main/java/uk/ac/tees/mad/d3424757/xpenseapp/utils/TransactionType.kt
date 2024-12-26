package uk.ac.tees.mad.d3424757.xpenseapp.utils

/**
 * Enum class to represent the types of transactions in the app.
 * - `INCOME`: For income transactions.
 * - `EXPENSE`: For expense transactions.
 *
 * @param displayName The user-friendly name to display for the transaction type.
 */
enum class TransactionType(val displayName: String) {
    INCOME("Income"),  // Represents an income transaction
    EXPENSE("Expense") // Represents an expense transaction
}

/**
 * Helper function to check if the transaction type is Income.
 *
 * @return True if the transaction type is Income, false otherwise.
 */
fun TransactionType.isIncome() = this == TransactionType.INCOME

/**
 * Extension function to get the display name of the transaction type.
 *
 * @return The user-friendly name of the transaction type (e.g., "Income" or "Expense").
 */
fun TransactionType.getDisplayName() = displayName
