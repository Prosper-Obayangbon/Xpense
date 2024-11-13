package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionType


class HomeViewModel(context: Context) : ViewModel() {

    private val dao = XpenseDatabase.getDatabase(context).appDao()


    // StateFlow to hold the list of transactions
    private val _transactions = MutableStateFlow<List<TransactionData>>(emptyList())
    val transactions: StateFlow<List<TransactionData>> get() = _transactions

    init {
        loadTransactions()
    }

    /**
     * Loads the transactions from the database and updates the state.
     */
    private fun loadTransactions() {
        viewModelScope.launch {
            dao.getTransactions().collect { transactionList ->
                _transactions.value = transactionList
            }
        }
    }

    /**
     * Adds a new transaction to the database.
     * @param transaction The transaction to be added.
     */
    fun addTransaction(transaction: TransactionData) {
        viewModelScope.launch {
            dao.insertTransaction(transaction)
        }
    }

    /**
     * Calculates the total balance based on transaction types.
     * @param list The list of transactions.
     * @return A formatted balance string.
     */
    @SuppressLint("DefaultLocale")
    fun getBalance(list: List<TransactionData>): String {
        val total = list.sumOf { if (it.type == TransactionType.INCOME) it.amount else -it.amount }
        return String.format("£ %.2f", total)
    }

    /**
     * Calculates the total expense from the list of transactions.
     * @param list The list of transactions.
     * @return A formatted expense string.
     */
    @SuppressLint("DefaultLocale")
    fun getTotalExpense(list: List<TransactionData>): String {
        val total = list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        return String.format("£ %.2f", total)
    }

    /**
     * Calculates the total income from the list of transactions.
     * @param list The list of transactions.
     * @return A formatted income string.
     */
    @SuppressLint("DefaultLocale")
    fun getTotalIncome(list: List<TransactionData>): String {
        val total = list.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        return String.format("£ %.2f", total)
    }
}
