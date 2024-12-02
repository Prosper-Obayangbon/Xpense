package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetData
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.BudgetWithSpent
import uk.ac.tees.mad.d3424757.xpenseapp.repository.BudgetRepository
import uk.ac.tees.mad.d3424757.xpenseapp.repository.TransactionRepository
import uk.ac.tees.mad.d3424757.xpenseapp.utils.getCurrentMonth

class BudgetViewModel(context : Context) : ViewModel() {

    private val budgetRepository: BudgetRepository

    init {
        val dao = XpenseDatabase.getDatabase(context = context)
        budgetRepository = BudgetRepository(dao)
        loadBudgetsForCurrentMonth()
    }

    val budgetsForMonth = MutableLiveData<List<BudgetWithSpent>>()

    // Function to fetch budgets for the current month
    private fun loadBudgetsForCurrentMonth() {
        val currentMonth = getCurrentMonth() // Format current month as "yyyy-MM"
        viewModelScope.launch {
            val budgets = budgetRepository.getBudgetsForMonth(currentMonth)
            budgetsForMonth.value = budgets
        }
    }
}

