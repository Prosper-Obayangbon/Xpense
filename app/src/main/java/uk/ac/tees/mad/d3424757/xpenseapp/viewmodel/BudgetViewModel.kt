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
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetData
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetWithSpent
import uk.ac.tees.mad.d3424757.xpenseapp.repository.BudgetRepository
import uk.ac.tees.mad.d3424757.xpenseapp.repository.TransactionRepository
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionCategories.getCategoriesForTransactionType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BudgetViewModel(context: Context) : ViewModel() {

    private val budgetRepository: BudgetRepository
    private val transactionRepository: TransactionRepository

    private val _budgetsForMonth = MutableStateFlow<List<BudgetData>>(emptyList())
    private val _calculatedBudgets = MutableStateFlow<List<BudgetWithSpent>>(emptyList())
    val calculatedBudgets: StateFlow<List<BudgetWithSpent>> = _calculatedBudgets

    var budgetAmount by mutableDoubleStateOf(0.0)
    var isAlertEnabled by mutableStateOf(false)
    var alertThreshold by mutableIntStateOf(80)

    private val _selectedCategory = MutableStateFlow("Category")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _currentMonth = MutableStateFlow(Calendar.getInstance())
    val currentMonth: StateFlow<Calendar> = _currentMonth

    // Format the month name dynamically
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

    private fun loadBudgetsForCurrentMonth() {
        viewModelScope.launch {
            try {
                val currentDate = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(_currentMonth.value.time)
                val budgets = budgetRepository.getBudgetsForMonth(currentDate)
                _budgetsForMonth.value = budgets

                // Calculate spent and remaining amounts
                val calculatedList = budgets.map { budget ->
                    val spent = calculateSpentAmount(budget.category) ?: 0.0
                    BudgetWithSpent(
                        id = budget.id,
                        category = budget.category,
                        amount = budget.amount,
                        spent = spent,
                        remainingAmount = budget.amount - spent
                    )
                }

                _calculatedBudgets.value = calculatedList
            } catch (e: Exception) {
                Log.e("BudgetViewModel", "Error loading budgets", e)
            }
        }
    }

    private suspend fun calculateSpentAmount(category: String): Double? {
        return try {
            val currentDate = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(_currentMonth.value.time)
            transactionRepository.loadTotalSpent(category, currentDate)
        } catch (e: Exception) {
            Log.e("BudgetViewModel", "Error calculating spent amount for $category", e)
            null
        }
    }

    fun onBudgetAmountChange(amount: Double) {
        budgetAmount = amount
    }

    fun onCategoryChange(category: String) {
        _selectedCategory.value = category
    }

    fun onDateChange(month: Calendar) {
        _currentMonth.value = month
    }

    fun onAlertToggle(isEnabled: Boolean) {
        isAlertEnabled = isEnabled
    }

    fun onAlertThresholdChange(percentage: Int) {
        alertThreshold = percentage
    }

    fun getCategories(): List<String> {
        val categories = getCategoriesForTransactionType(isIncome = false)
        return categories.map { it.name }
    }

    fun saveBudget(budget: BudgetData) {
        viewModelScope.launch {
            budgetRepository.insertBudget(budget)
        }
    }

    fun deleteBudget(id :Int){
        viewModelScope.launch {
            budgetRepository.deleteBudget(id)
        }
    }

    fun validateBudget(): Boolean {
        return when {
            budgetAmount <= 0.0 -> {
                _errorMessage.value = "Budget amount must be greater than 0."
                false
            }
            _selectedCategory.value.isBlank() -> {
                _errorMessage.value = "Please select a category."
                false
            }
            else -> {
                _errorMessage.value = ""
                true
            }
        }
    }
}
