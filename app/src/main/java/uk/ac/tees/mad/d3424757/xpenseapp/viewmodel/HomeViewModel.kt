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
import uk.ac.tees.mad.d3424757.xpenseapp.repository.TransactionRepository
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionType

/**
 * ViewModel for the Home screen, responsible for managing transactions.
 * Uses the TransactionRepository to load and insert data.
 */
class HomeViewModel(context: Context) : ViewModel() {

    private val repository: TransactionRepository

    // StateFlow to hold the list of transactions.
    private val _transactions = MutableStateFlow<List<TransactionData>>(emptyList())
    val transactions: StateFlow<List<TransactionData>> get() = _transactions

    // Initialize the repository with the DAO from the database.
    init {
        val dao = XpenseDatabase.getDatabase(context)
        repository = TransactionRepository(dao)
        getTransactions()
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
    fun addTransaction(transaction: TransactionData) {
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }

    /**
     * Calculates the total balance based on transaction types (income or expense).
     * @param list The list of transactions to calculate the balance from.
     * @return A formatted balance string with the currency symbol.
     */
    @SuppressLint("DefaultLocale")
    fun getBalance(list: List<TransactionData>): String {
        val total = list.sumOf { if (it.type == TransactionType.INCOME) it.amount else -it.amount }
        return String.format("£ %.2f", total)
    }

    /**
     * Calculates the total expense from the list of transactions.
     * @param list The list of transactions to calculate the total expense from.
     * @return A formatted expense string with the currency symbol.
     */
    @SuppressLint("DefaultLocale")
    fun getTotalExpense(list: List<TransactionData>): String {
        val total = list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        return String.format("£ %.2f", total)
    }

    /**
     * Calculates the total income from the list of transactions.
     * @param list The list of transactions to calculate the total income from.
     * @return A formatted income string with the currency symbol.
     */
    @SuppressLint("DefaultLocale")
    fun getTotalIncome(list: List<TransactionData>): String {
        val total = list.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        return String.format("£ %.2f", total)
    }
}
