package uk.ac.tees.mad.d3424757.xpenseapp.utils

import androidx.compose.ui.graphics.Color
import uk.ac.tees.mad.d3424757.xpenseapp.R

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

/**
 * Returns an icon and color based on the transaction category.
 * @param category The category of the transaction (e.g., "Food", "Shopping").
 * @return A Pair consisting of the icon resource ID and the color associated with the category.
 */
fun getIconAndColor(category: String): Pair<Int, Color> {
    return when (category) {
        "Food" -> Pair(R.drawable.ic_google_icon, Color(0xFFFFC8C7)) // Example icon and color for food
        "Shopping" -> Pair(R.drawable.transaction, Color(0xFFFFD2C4)) // Example icon and color for shopping
        "Salary" -> Pair(R.drawable.ic_google_icon, Color(0xFFDEF7E7)) // Example icon and color for salary
        else -> Pair(R.drawable.ic_google_icon, Color.Gray) // Default icon and color for other categories
    }
}