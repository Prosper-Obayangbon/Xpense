package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetData
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetWithSpent
import uk.ac.tees.mad.d3424757.xpenseapp.repository.BudgetRepository
import uk.ac.tees.mad.d3424757.xpenseapp.repository.TransactionRepository
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.DEFAULT_ALERT_THRESHOLD
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.DEFAULT_BUDGET_AMOUNT
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.DEFAULT_CATEGORY
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_BUDGET_AMOUNT_INVALID
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_CATEGORY_INVALID
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionCategories.getCategoriesForTransactionType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * ViewModel for managing and processing budgets in the Xpense app.
 */
class BudgetViewModel(context: Context) : ViewModel() {


    private val budgetRepository: BudgetRepository
    private val transactionRepository: TransactionRepository

    // Internal mutable state flows
    private val _budgetsForMonth = MutableStateFlow<List<BudgetData>>(emptyList())
    private val _calculatedBudgets = MutableStateFlow<List<BudgetWithSpent>>(emptyList())
    val calculatedBudgets: StateFlow<List<BudgetWithSpent>> = _calculatedBudgets

    // Observable state variables
    var budgetAmount: Double by mutableDoubleStateOf(DEFAULT_BUDGET_AMOUNT)
    var currentId: Int by mutableIntStateOf(0)
    var isAlertEnabled by mutableStateOf(false)
    var alertThreshold by mutableIntStateOf(DEFAULT_ALERT_THRESHOLD)
    private var _selectedCategory = MutableStateFlow(DEFAULT_CATEGORY)
    val selectedCategory: StateFlow<String> = _selectedCategory
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage
    private val _currentMonth = MutableStateFlow(Calendar.getInstance())
    val currentMonth: StateFlow<Calendar> = _currentMonth

    // Dynamically formatted month name
    val monthName = _currentMonth.map { calendar ->
        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
    }.stateIn(viewModelScope, SharingStarted.Lazily, "")

    init {
        val dao = XpenseDatabase.getDatabase(context)
        budgetRepository = BudgetRepository(dao)
        transactionRepository = TransactionRepository(dao)

        loadBudgetsForCurrentMonth()

        // Observe changes in the month and reload budgets
        viewModelScope.launch {
            currentMonth.collect {
                loadBudgetsForCurrentMonth()
            }
        }
    }

    /**
     * Load budgets for the current month.
     */
    private fun loadBudgetsForCurrentMonth() {
        viewModelScope.launch {
            try {
                val currentDate = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(_currentMonth.value.time)
                val budgets = budgetRepository.getBudgetsForMonth(currentDate)
                _budgetsForMonth.value = budgets

                // Calculate spent and remaining amounts for each budget
                val calculatedList = budgets.map { budget ->
                    val spent = calculateSpentAmount(budget.category) ?: 0.0
                    BudgetWithSpent(
                        id = budget.id,
                        category = budget.category,
                        amount = budget.amount,
                        spent = spent
                    )
                }

                _calculatedBudgets.value = calculatedList
            } catch (e: Exception) {
                Log.e("BudgetViewModel", "Error loading budgets", e)
            }
        }
    }

    /**
     * Calculate the total spent amount for a given category.
     */
    private suspend fun calculateSpentAmount(category: String): Double? {
        return try {
            val currentDate = SimpleDateFormat("yyyy/MM", Locale.getDefault()).format(_currentMonth.value.time)
            transactionRepository.loadTotalSpent(category, currentDate)
        } catch (e: Exception) {
            Log.e("BudgetViewModel", "Error calculating spent amount for $category", e)
            null
        }
    }

    /**
     * Handle budget amount changes.
     */
    fun onBudgetAmountChange(amount: Double) {
        budgetAmount = amount
    }

    /**
     * Handle category selection changes.
     */
    fun onCategoryChange(category: String) {
        _selectedCategory.value = category
    }

    /**
     * Handle month selection changes.
     */
    fun onDateChange(month: Calendar) {
        _currentMonth.value = month
    }

    /**
     * Toggle the alert enabled state.
     */
    fun onAlertToggle(isEnabled: Boolean) {
        isAlertEnabled = isEnabled
    }

    /**
     * Handle alert threshold percentage changes.
     */
    fun onAlertThresholdChange(percentage: Int) {
        alertThreshold = percentage
    }

    /**
     * Get available categories for budget.
     */
    fun getCategories(): List<String> {
        return getCategoriesForTransactionType(isIncome = false).map { it.name }
    }

    /**
     * Save a new budget to the repository.
     */
    fun saveBudget(budget: BudgetData) {
        viewModelScope.launch {
            budgetRepository.insertBudget(budget)
        }
    }

    /**
     * Delete a budget by its ID.
     */
    fun deleteBudget(id: Int) {
        viewModelScope.launch {
            budgetRepository.deleteBudget(id)
        }
    }

    /**
     * Update an existing budget.
     */
    fun updateBudget(budget: BudgetData) {
        viewModelScope.launch {
            budgetRepository.updateBudget(budget)
        }
    }

    /**
     * Fetch a budget by its ID.
     */
    suspend fun getBudgetById(budgetId: Int): BudgetData {
        return budgetRepository.getBudgetById(budgetId)
    }

    /**
     * Initialize the view model with an existing budget's data.
     */
    fun initializeBudget(budgetId: Int) {
        if (budgetId != -1) { // If the budget ID is valid, fetch the budget data
            viewModelScope.launch {
                try {
                    val budget = budgetRepository.getBudgetById(budgetId)
                    currentId = budget.id
                    budgetAmount = budget.amount
                    _selectedCategory.value = budget.category
                    isAlertEnabled = budget.alertEnabled
                    alertThreshold = budget.alertThreshold
                } catch (e: Exception) {
                    Log.e("BudgetViewModel", "Error initializing budget", e)
                }
            }
        }
    }

    /**
     * Validate the budget input.
     */
    fun validateBudget(): Boolean {
        return when {
            budgetAmount <= 0.0 -> {
                _errorMessage.value = ERROR_BUDGET_AMOUNT_INVALID
                false
            }
            _selectedCategory.value.isBlank() -> {
                _errorMessage.value = ERROR_CATEGORY_INVALID
                false
            }
            else -> {
                _errorMessage.value = ""
                true
            }
        }
    }
}
