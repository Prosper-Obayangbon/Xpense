import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.TransactionData
import uk.ac.tees.mad.d3424757.xpenseapp.repository.TransactionRepository
import uk.ac.tees.mad.d3424757.xpenseapp.repository.UserProfileRepository
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.AFTERNOON_START_HOUR
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.EVENING_START_HOUR
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.MORNING_START_HOUR
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.NIGHT_END_HOUR
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.NIGHT_START_HOUR
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionType
import java.time.LocalTime

class HomeViewModel(context: Context) : ViewModel() {

    // State to hold the user's name.
    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> = _userName

    // Repositories to fetch data.
    private val transactionRepository: TransactionRepository
    private val userRepository: UserProfileRepository

    // StateFlow to hold the list of transactions.
    private val _transactions = MutableStateFlow<List<TransactionData>>(emptyList())
    val transactions: StateFlow<List<TransactionData>> get() = _transactions


    // Initialize repositories with database.
    init {
        val dao = XpenseDatabase.getDatabase(context)
        transactionRepository = TransactionRepository(dao)
        userRepository = UserProfileRepository(dao)

        // Fetch the user's name and transactions.
        getUserName()
        getTransactions()
    }

    /**
     * Fetches the user's name from the repository.
     * If the name is not found, it sets it as an empty string.
     */
    private fun getUserName() {
        viewModelScope.launch {
            _userName.value = userRepository.getUserProfile()?.name ?: ""
        }
    }

    /**
     * Loads transactions from the repository and updates the state.
     */
    private fun getTransactions() {
        viewModelScope.launch {
            transactionRepository.loadTransactions().collect { transactionList ->
                _transactions.value = transactionList
            }
        }
    }

    /**
     * Calculates the balance based on transaction types (income or expense).
     * @param transactions The list of transactions to calculate the balance from.
     * @return A formatted balance string with the currency symbol.
     */
    fun calculateBalance(transactions: List<TransactionData>): String {
        val total = transactions.sumOf {
            if (it.type == TransactionType.INCOME) it.amount else -it.amount
        }

        return formatCurrency(total)
    }

    /**
     * Generalized method to calculate total amount for either income or expense.
     * @param transactions The list of transactions to calculate from.
     * @param type The type of transaction (income or expense).
     * @return A formatted string with the total amount.
     */
    private fun calculateTotalByType(transactions: List<TransactionData>, type: TransactionType): String {
        val total = transactions.filter { it.type == type }.sumOf { it.amount }
        return formatCurrency(total)
    }

    /**
     * Helper function to format the amount as currency.
     * @param amount The amount to format.
     * @return A string formatted as currency.
     */
    private fun formatCurrency(amount: Double): String {
        return String.format("£ %.2f", amount)
    }

    /**
     * Calculates the total expense from the list of transactions.
     * @param transactions The list of transactions to calculate from.
     * @return A formatted expense string with the currency symbol.
     */
    fun getTotalExpense(transactions: List<TransactionData>): String {
        return calculateTotalByType(transactions, TransactionType.EXPENSE)
    }

    /**
     * Calculates the total income from the list of transactions.
     * @param transactions The list of transactions to calculate from.
     * @return A formatted income string with the currency symbol.
     */
    fun getTotalIncome(transactions: List<TransactionData>): String {
        return calculateTotalByType(transactions, TransactionType.INCOME)
    }

    /**
     * Determines the greeting based on the current time of day.
     * @return The greeting message ("Good Morning", "Good Afternoon", or "Good Night").
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getGreeting(): String {
        val hourOfDay = LocalTime.now().hour
        return when {
            hourOfDay in MORNING_START_HOUR..AFTERNOON_START_HOUR - 1 -> "Good Morning"
            hourOfDay in AFTERNOON_START_HOUR..EVENING_START_HOUR - 1 -> "Good Afternoon"
            hourOfDay in NIGHT_START_HOUR downTo NIGHT_END_HOUR -> "Good Night"
            else -> "Good Night"
        }
    }
}
