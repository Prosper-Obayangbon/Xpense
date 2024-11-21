package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import uk.ac.tees.mad.d3424757.xpenseapp.repository.TransactionRepository
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Category
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionCategories.getCategoriesForTransactionType
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionType
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TransactionViewModel(context : Context) : ViewModel() {
    private val repository: TransactionRepository

    // StateFlow to hold the list of transactions.
    private val _transactions = MutableStateFlow<List<TransactionData>>(emptyList())
    val transactions: StateFlow<List<TransactionData>> get() = _transactions


    // Amount entered by the user, stored as a StateFlow for UI observation.
    private val _amount = MutableStateFlow("")
    val amount = _amount.asStateFlow()

    // Description entered by the user, stored as a StateFlow for UI observation.
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    // Selected category for the transaction (Income/Expense), stored as a StateFlow for UI observation.
    private var _selectedCategory = MutableStateFlow<Category?>(null)
    var selectedCategory = _selectedCategory.asStateFlow()

    // Selected date for the transaction, stored as a StateFlow for UI observation.
    private val _selectedDate = MutableStateFlow(getCurrentDate()) // Default to current date
    val selectedDate = _selectedDate.asStateFlow()

    // Error message to be displayed in the UI, if any validation fails.
    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()



    private val _filteredAndGroupedTransactions = MutableStateFlow<Map<String, List<TransactionData>>>(emptyMap())
    val filteredAndGroupedTransactions: StateFlow<Map<String, List<TransactionData>>> = _filteredAndGroupedTransactions


    // Initialize the repository with the DAO from the database.
    init {
        val dao = XpenseDatabase.getDatabase(context)
        repository = TransactionRepository(dao)
        getTransactions()
    }

    /**
     * Gets the available categories based on whether the transaction is income or expense.
     */
    fun getCategories(isIncome: Boolean): List<Category> {
        return getCategoriesForTransactionType(isIncome)
    }

    /**
     * Updates the amount entered by the user.
     * This function can be extended with validation logic.
     */
    fun updateAmount(newAmount: String) {
        if (newAmount.toDoubleOrNull() != null && newAmount.toDouble() > 0) {
            _amount.value = newAmount
        } else {
            _errorMessage.value = "Please enter a valid amount greater than 0."
        }
    }

    /**
     * Updates the description of the transaction.
     */
    fun updateDescription(newDescription: String) {
        _description.value = newDescription
    }

    /**
     * Updates the selected category for the transaction.
     */
    fun updateCategory(newCategory: Category) {
        _selectedCategory.value = newCategory
    }

    /**
     * Updates the selected date for the transaction.
     */
    fun updateDate(newDate: String) {
        Log.d("DateUpdate", "Selected Date: $newDate")
        _selectedDate.value = newDate
    }

    /**
     * Validates the transaction data before submission.
     * Returns true if all fields are valid, false otherwise.
     */
    fun validateTransaction(): Boolean {
        return when {
            amount.value.isEmpty() -> {
                _errorMessage.value = "Amount cannot be empty."
                false
            }
            selectedCategory.value == null -> {
                _errorMessage.value = "Please select a category."
                false
            }
            selectedDate.value.isEmpty() -> {
                _errorMessage.value = "Date cannot be empty."
                false
            }
            else -> {
                _errorMessage.value = "" // Clear any previous error message
                true
            }
        }
    }

    /**
     * Loads the transactions from the repository.
     * It collects the Flow from the repository and updates the state with the list of transactions.
     */
    private fun getTransactions() {
        viewModelScope.launch {
            repository.loadTransactions().collect { transactionList ->
                _transactions.value = transactionList
            }
        }
    }


    /**
     * Adds a new transaction to the database using the repository.
     *
     * @param transaction The transaction to be added.
     */
    fun addTransaction(isIncome: Boolean) {
        var type = TransactionType.EXPENSE
        if(isIncome) {
            type = TransactionType.INCOME
        }
        viewModelScope.launch {
            repository.insertTransaction(
                TransactionData(
                    category = selectedCategory.value!!.name,
                    description = _description.value,
                    amount = _amount.value.toDouble(),
                    time = getFormattedCurrentTime(),
                    date = _selectedDate.value,
                    type = type,

                )
            )
        }
    }

    /**
     * Resets all fields to their initial state after a transaction is saved or canceled.
     */
    fun resetTransaction() {
        _selectedCategory.value = null
        _amount.value = ""
        _description.value = ""
        _selectedCategory.value = null
        _selectedDate.value = getCurrentDate() // Reset to the current date
    }

    /**
     * Helper function to get the current date in the format: Day/Month/Year.
     */
    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        return "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
    }

    @SuppressLint("SimpleDateFormat")
    fun getFormattedCurrentTime(): String {
        val dateFormat = SimpleDateFormat("hh:mm a") // 12-hour format with AM/PM
        val currentTime = Date() // Get the current date and time
        return dateFormat.format(currentTime) // Return formatted time (e.g., 02:30 PM)
    }







}
