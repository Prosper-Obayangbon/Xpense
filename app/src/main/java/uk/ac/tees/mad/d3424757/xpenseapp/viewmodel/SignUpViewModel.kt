package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3424757.xpenseapp.repository.AuthRepository

class SignUpViewModel : ViewModel() {
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
     * Initiates sign-up if all validation checks pass.
     * @param onComplete callback indicating success or failure of sign-up
     */
    fun signUpUser(onComplete: (Boolean) -> Unit) {
        when (val validationResult = validateInputs()) {
            is ValidationResult.Success -> executeSignUp(onComplete)
            is ValidationResult.Error -> {
                _error.value = validationResult.message
                onComplete(false)
            }
        }
    }

    // Validation logic encapsulated in a sealed class
    private fun validateInputs(): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Error("Name cannot be empty.")
            email.isBlank() -> ValidationResult.Error("Email cannot be empty.")
            password.isBlank() -> ValidationResult.Error("Password cannot be empty.")
            !password.matches(passwordPattern) -> ValidationResult.Error(
                "Password must contain at least 8 characters, including uppercase, lowercase, number, and symbol."
            )
            else -> ValidationResult.Success
        }
    }

    // Executes the sign-up process by calling AuthRepository
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

    // Functions to update state
    fun updateName(newName: String) { _name.value = newName }
    fun updateEmail(newEmail: String) { _email.value = newEmail }
    fun updatePassword(newPassword: String) { _password.value = newPassword }

    // Sealed class to handle validation outcomes
    private sealed class ValidationResult {
        data object Success : ValidationResult()
        data class Error(val message: String) : ValidationResult()
    }
}
