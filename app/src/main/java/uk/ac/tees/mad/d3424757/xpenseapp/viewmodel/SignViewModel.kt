package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3424757.xpenseapp.data.preferences.UserPreferences
import uk.ac.tees.mad.d3424757.xpenseapp.repository.AuthRepository

class SignViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    // Backing properties with mutable state
    private var _name = mutableStateOf("")
    private var _email = mutableStateOf("")
    private var _password = mutableStateOf("")
    private var _error = mutableStateOf<String?>(null)

    // Public read-only properties
    val name: String get() = _name.value
    val email: String get() = _email.value
    val password: String get() = _password.value
    val error: String? get() = _error.value

    // Regular expression for password complexity requirements
    private val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")

    /**
     * Initiates the sign-up process if all validation checks pass.
     * @param onComplete Callback indicating the success or failure of sign-up
     */
    fun signUpUser(onComplete: (Boolean) -> Unit) {
        validateInputs()?.let { errorMessage ->
            _error.value = errorMessage
            onComplete(false)
        } ?: run {
            executeSignUp(onComplete)
        }
    }

    /**
     * Validates user inputs and returns an error message if validation fails.
     * @return Error message or null if validation is successful
     */
    private fun validateInputs(): String? {
        return when {
            name.isBlank() -> "Name cannot be empty."
            email.isBlank() -> "Email cannot be empty."
            password.isBlank() -> "Password cannot be empty."
            !password.matches(passwordPattern) -> "Password must contain at least 8 characters, including uppercase, lowercase, number, and symbol."
            else -> null
        }
    }

    /**
     * Executes the sign-up process by calling AuthRepository.
     * @param onComplete Callback indicating the success or failure of the sign-up
     */
    private fun executeSignUp(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            authRepository.signUpWithEmail(email, password) { success, errorMessage ->
                if (success) {
                    _error.value = null // Clear any existing errors
                    onComplete(true)
                } else {
                    _error.value = errorMessage ?: "Sign-up failed. Please try again."
                    onComplete(false)
                }
            }
        }
    }

    /**
     * Handles user login.
     * @param onComplete Callback indicating the success or failure of the login
     */
    fun executeSignIn(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            authRepository.signInWithEmail(email, password) { success, errorMessage ->
                if (success) {
                    onComplete(true)
                } else {
                    _error.value =  "Incorrect Email or Password."
                    onComplete(false)
                }
            }
        }
    }

    // Function to handle Google Sign-In
    fun executeGoogleSignIn(idToken: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            authRepository.signInWithGoogle(idToken) { success, errorMessage ->
                if(success){
                    onComplete(true)
                } else {
                    _error.value = "Failed! Try again later."
                    onComplete(false)
                }
            }
        }
    }

    /**
     * Updates the user's registration status in preferences.
     * @param context Application context
     */
    fun updateUserRegistrationStatus(context: Context) {
        val userPreferences = UserPreferences(context)
        userPreferences.setUserRegistered(true)
    }

    // Functions to update state
    fun updateName(newName: String) { _name.value = newName }
    fun updateEmail(newEmail: String) { _email.value = newEmail }
    fun updatePassword(newPassword: String) { _password.value = newPassword }
}
