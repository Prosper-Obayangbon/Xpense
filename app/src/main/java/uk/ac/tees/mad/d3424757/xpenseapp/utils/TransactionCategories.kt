package uk.ac.tees.mad.d3424757.xpenseapp.utils

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import uk.ac.tees.mad.d3424757.xpenseapp.R

// Data class representing a Category with a name, icon, and color.
data class Category(
    val name: String,  // The name of the category (e.g., "Salary", "Food")
    val icon: Int,     // Resource ID for the icon associated with this category
    val color: Color   // The color associated with this category
)

// Object to store predefined lists of income and expense categories.
object TransactionCategories {

    // List of income categories, each having a name, icon, and color.
    private val incomeCategories = listOf(
        Category(name = "Salary", icon = R.drawable.salary, color = Color.Blue),
        Category(name = "Investments Returns", icon = R.drawable.trendup, color = Color(0xFF009688)),
        Category(name = "Bonus", icon = R.drawable.gift, color = Color.Green),
        Category(name = "Interest Income", icon = R.drawable.money, color = Color(0xffFFC107)),
        Category(name = "Rental Income", icon = R.drawable.home, color = Color(0xFF795548)),
        Category(name = "Freelancing", icon = R.drawable.create, color = Color(0xFF673AB7)),
        Category(name = "Refund", icon = R.drawable.returnback, color = Color.Cyan),
        Category(name = "Other Income", icon = R.drawable.add, color = Color.Gray),
    )

    // List of expense categories, each having a name, icon, and color.
    private val expenseCategories = listOf(
        Category(name = "Food", icon = R.drawable.restaurant, color = Color(0xFFFF5722)),
        Category(name = "Transportation", icon = R.drawable.car, color = Color.Blue),
        Category(name = "Housing and Rent", icon = R.drawable.home, color = Color(0xFF795548)),
        Category(name = "Utility", icon = R.drawable.electrical_services, color = Color.Yellow),
        Category(name = "Entertainment", icon = R.drawable.movie, color = Color.Red),
        Category(name = "Healthcare", icon = R.drawable.medical_services, color = Color(0xFFF06292)),
        Category(name = "Education", icon = R.drawable.school, color = Color.Green),
        Category(name = "Shopping", icon = R.drawable.shopping_bag, color = Color(0xFF673AB7)),
        Category(name = "Travel", icon = R.drawable.flight, color = Color.Cyan),
        Category(name = "Subscription", icon = R.drawable.recurring_bill, color = Color(0xFF3F51B5)),
        Category(name = "Insurance", icon = R.drawable.description, color = Color(0xFF607D8B)),
        Category(name = "Taxes", icon = R.drawable.baseline_receipt_24 , color = Color.Red),
        Category(name = "Gifts and Donations", icon = R.drawable.gift, color = Color(0xFFF48FB1)),
        Category(name = "Other Expenses", icon = R.drawable.remove, color = Color.Gray)
    )

    /**
     * Returns the icon and color for a given category.
     * @param categoryName The name of the category (e.g., "Food", "Salary").
     * @return A Pair containing the icon resource ID and the color for the category, or null if the category is not found.
     */
    fun getIconAndColor(categoryName: String): Pair<Int, Color>? {
        // Check if the category name matches any income categories
        incomeCategories.forEach {
            if (categoryName == it.name) {
                return Pair(it.icon, it.color)
            }
        }

        // Check if the category name matches any expense categories
        expenseCategories.forEach {
            if (categoryName == it.name) {
                return Pair(it.icon, it.color)
            }
        }

        // Return null if no matching category was found
        return null
    }

    /**
     * Returns a list of categories based on whether the transaction is income or expense.
     * @param isIncome A Boolean indicating if the transaction is income (`true`) or expense (`false`).
     * @return A list of Category objects either from income or expense categories.
     */
    fun getCategoriesForTransactionType(isIncome: Boolean): List<Category> {
        // Return income categories if isIncome is true, otherwise return expense categories
        return if (isIncome) incomeCategories else expenseCategories
    }
}
