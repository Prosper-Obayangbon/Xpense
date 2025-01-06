package uk.ac.tees.mad.d3424757.xpenseapp.utils

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import uk.ac.tees.mad.d3424757.xpenseapp.R
import java.time.format.DateTimeFormatter
import java.util.Locale


object Constants {
    // Constants for animation
    const val ANIMATION_DURATION = 800 // Duration for the scale animation in milliseconds
    const val SPLASH_DELAY = 2000L // Delay before navigating to the next screen

    const val RC_SIGN_IN = 9001
    const val DATABASE_NAME = "XpenseDB"
    // Error messages
    const val ERROR_AMOUNT_EMPTY = "Amount cannot be empty."
    const val ERROR_INVALID_AMOUNT = "Please enter a valid amount greater than 0."
    const val ERROR_CATEGORY_EMPTY = "Please select a category."
    const val ERROR_DATE_EMPTY = "Date cannot be empty."

    // Default date format
    const val DATE_FORMAT = "yyyy/MM/dd"
    const val TIME_FORMAT = "hh:mm a"

    @RequiresApi(Build.VERSION_CODES.O)
    val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

    // Success and error messages
    const val PROFILE_UPDATE_SUCCESS = "Update successfully!"
    const val PASSWORD_CHANGE_SUCCESS = "Password changed successfully!"
    const val PASSWORD_MISMATCH_ERROR = "Passwords do not match"
    const val PROFILE_PICTURE_UPDATE_ERROR = "Failed to update profile picture"
    const val PASSWORD_CHANGE_ERROR = "Error: Password change failed"

    // Constants for greeting time ranges.

    const val MORNING_START_HOUR = 5
    const val AFTERNOON_START_HOUR = 12
    const val EVENING_START_HOUR = 17
    const val NIGHT_START_HOUR = 18
    const val NIGHT_END_HOUR = 4

    const val DEFAULT_BUDGET_AMOUNT = 0.0
    const val DEFAULT_CATEGORY = "Category"
    const val DEFAULT_ALERT_THRESHOLD = 80
    const val ERROR_BUDGET_AMOUNT_INVALID = "Budget amount must be greater than zero. Please enter a valid amount."
    const val ERROR_CATEGORY_INVALID = "Please select a category from the list."

    // Regular expression for password complexity requirements
    val PASSWORD_PATTERN = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")
    val FULL_NAME_PATTERN = Regex("^[A-Za-z]+(?:\\s[A-Za-z]+)+$")


    // Error messages
    const val ERROR_NAME_EMPTY = "Name cannot be empty."
    const val ERROR_EMAIL_EMPTY = "Email cannot be empty."
    const val ERROR_PASSWORD_EMPTY = "Password cannot be empty."
    const val ERROR_PASSWORD_COMPLEXITY = "Password must contain at least 8 characters, including uppercase, lowercase, number, and symbol."
    const val ERROR_FULL_NAME = "Full name must contain at least two words with only alphabetic characters and a single space between words."
    const val ERROR_SIGNUP_FAILED = "Sign-up failed. Please try again."
    const val ERROR_SAVE_PROFILE_FAILED = "Error saving user profile: "
    const val ERROR_INCORRECT_CREDENTIALS = "Incorrect Email or Password."
    const val ERROR_GOOGLE_SIGNIN_FAILED = "Failed! Try again later."
    const val PROFILE_LOAD_ERROR = "Failed to load user profile"

    // Transaction types for filtering
    val TRANSACTION_TYPES = listOf("All", "Income", "Expense")// Categories for transactions (Income or Expense) used in filtering.


    // Error and success messages.
    const val ERROR_MSG_NO_DATA = "No Transaction data available"
    const val CATEGORY_NAME_EXPENSE = "Expense"
    const val CATEGORY_NAME_INCOME = "Income"

    const val TRANSACTION_NOT_FOUND = "Not found"
    const val TRANSACTION_DETAILS_TITLE = "Transaction Details"
    const val TRANSACTION_CATEGORY_LABEL = "Category"
    const val TRANSACTION_DESCRIPTION_LABEL = "Description"
    const val TRANSACTION_STATUS_LABEL = "Status"
    const val TRANSACTION_DATE_LABEL = "Date"
    const val TRANSACTION_TIME_LABEL = "Time"
    const val CIRCLE_ICON_SIZE = 100






    val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    val MONTH_FILTER = listOf("All", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")


}
