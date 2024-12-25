import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import uk.ac.tees.mad.d3424757.xpenseapp.repository.TransactionRepository
import uk.ac.tees.mad.d3424757.xpenseapp.repository.UserProfileRepository
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionType
import java.time.LocalTime

class HomeViewModel(context: Context) : ViewModel() {
    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> = _userName

    private val transactionRepository: TransactionRepository
    private val userRepository: UserProfileRepository

    // StateFlow to hold the list of transactions.
    private val _transactions = MutableStateFlow<List<TransactionData>>(emptyList())
    val transactions: StateFlow<List<TransactionData>> get() = _transactions

    // Initialize the repository with the DAO from the database.
    init {
        val dao = XpenseDatabase.getDatabase(context)
        transactionRepository = TransactionRepository(dao)
        userRepository = UserProfileRepository(dao)

        // Get the username from the database
        getUserName()

        // Load the transactions
        getTransactions()
    }

    /**
     * Loads the transactions from the repository.
     * It collects the Flow from the repository and updates the state with the list of transactions.
     */
    private fun getTransactions() {
        viewModelScope.launch {
            transactionRepository.loadTransactions().collect { transactionList ->
                _transactions.value = transactionList
            }
        }
    }

    /**
     * Fetches the user's name from the repository.
     * If the name is not found, it sets it as an empty string.
     */
    private fun getUserName() {
        viewModelScope.launch {
            _userName.value = userRepository.getUserProfile(0)?.name ?: ""
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

        if(total < 0){
            return String.format("£ %.2f", 0.0)
        }
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

    /**
     * Determines the greeting based on the current time of day.
     * @return The greeting message ("Good Morning", "Good Afternoon", or "Good Night").
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getGreeting(): String {
        val hourOfDay = LocalTime.now().hour
        return when {
            hourOfDay in 5..11 -> "Good Morning"
            hourOfDay in 12..17 -> "Good Afternoon"
            else -> "Good Night"
        }
    }
}
