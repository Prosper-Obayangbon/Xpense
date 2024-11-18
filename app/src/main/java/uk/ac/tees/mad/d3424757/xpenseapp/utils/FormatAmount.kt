package uk.ac.tees.mad.d3424757.xpenseapp.utils

/**
 * Formats the amount based on the transaction type (INCOME/EXPENSE).
 * @param amount The amount to be formatted.
 * @param type The type of transaction, either "INCOME" or "EXPENSE".
 * @return A string representing the formatted amount with the currency symbol.
 */
fun formatAmount(amount: Double, type: String): String {
    return if (type == "INCOME") {
        // If income, format as positive value
        "£ $amount"
    } else {
        // If expense, format as negative value
        "- £$amount"
    }
}

