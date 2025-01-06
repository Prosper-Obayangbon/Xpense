package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.UserProfile
import uk.ac.tees.mad.d3424757.xpenseapp.data.preferences.UserPreferences
import uk.ac.tees.mad.d3424757.xpenseapp.repository.AuthRepository
import uk.ac.tees.mad.d3424757.xpenseapp.repository.UserProfileRepository
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_EMAIL_EMPTY
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_FULL_NAME
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_GOOGLE_SIGNIN_FAILED
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_NAME_EMPTY
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_PASSWORD_COMPLEXITY
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_PASSWORD_EMPTY
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_SAVE_PROFILE_FAILED
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_SIGNUP_FAILED
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.FULL_NAME_PATTERN
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.PASSWORD_PATTERN

class AuthViewModel(context: Context) : ViewModel() {

    private val authRepository = AuthRepository()
    private val dao = XpenseDatabase.getDatabase(context)
    private val repository = UserProfileRepository(dao)

    // State variables
    private var _name = mutableStateOf("")
    private var _email = mutableStateOf("")
    private var _password = mutableStateOf("")
    private var _error = mutableStateOf<String?>(null)

    // Public properties
    val fullName: String get() = _name.value
    val email: String get() = _email.value
    val password: String get() = _password.value
    val error: String? get() = _error.value

    /**
     * Initiates the sign-up process.
     */
    fun signUpUser(onComplete: (Boolean) -> Unit) {
        validateInputs()?.let { errorMessage ->
            _error.value = errorMessage
            onComplete(false)
        } ?: executeSignUp(fullName, email, "", onComplete)
    }

    /**
     * Validates user inputs.
     */
    private fun validateInputs(): String? {
        return when {
            fullName.isBlank() -> ERROR_NAME_EMPTY
            !fullName.matches(FULL_NAME_PATTERN) -> ERROR_FULL_NAME
            email.isBlank() -> ERROR_EMAIL_EMPTY
            password.isBlank() -> ERROR_PASSWORD_EMPTY
            !password.matches(PASSWORD_PATTERN) -> ERROR_PASSWORD_COMPLEXITY
            else -> null
        }
    }

    /**
     * Executes sign-up logic and saves the user profile.
     */
    private fun executeSignUp(
        fullName: String,
        username: String,
        profilePicture: String?,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            authRepository.signUpWithEmail(email, password) { success, errorMessage ->
                if (success) {
                    _error.value = null
                    val nameParts = fullName.trim().split(" ")
                    insertUserProfile(
                        firstName = nameParts.first(),
                        lastName = nameParts.drop(1).joinToString(" "),
                        username,
                        profilePicture,
                        onComplete
                    )
                } else {
                    _error.value = errorMessage ?: ERROR_SIGNUP_FAILED
                    onComplete(false)
                }
            }
        }
    }

    private fun insertUserProfile(
        firstName: String,
        lastName: String,
        username: String,
        profilePicture: String?,
        onComplete: (Boolean) -> Unit
    ) {
        val userProfile = UserProfile(
            firstName = firstName,
            lastName = lastName,
            email = username,
            profilePicture = profilePicture.orEmpty()
        )

        viewModelScope.launch {
            try {
                repository.saveUserProfile(userProfile)
                onComplete(true)
            } catch (e: Exception) {
                _error.value = "$ERROR_SAVE_PROFILE_FAILED ${e.message}"
                onComplete(false)
            }
        }
    }

    /**
     * Handles user login.
     */
    fun executeSignIn(onComplete: (Boolean) -> Unit) {
        validateSign()?.let { errorMessage ->
            _error.value = errorMessage
            onComplete(false)
        } ?: viewModelScope.launch {
            authRepository.signInWithEmail(email, password) { success, errorMessage ->
                if (success) _error.value = null else _error.value = errorMessage
                onComplete(success)
            }
        }
    }

    private fun validateSign(): String? {
        return when {
            email.isBlank() -> ERROR_EMAIL_EMPTY
            password.isBlank() -> ERROR_PASSWORD_EMPTY
            else -> null
        }
    }

    /**
     * Handles Google Sign-In.
     */
    fun executeGoogleSignIn(idToken: String?, onComplete: (Boolean) -> Unit) {
        if (idToken == null) {
            _error.value = ERROR_GOOGLE_SIGNIN_FAILED
            onComplete(false)
            return
        }

        viewModelScope.launch {
            authRepository.signInWithGoogle(idToken) { success, errorMessage ->
                if (success) {
                    _error.value = null
                    insertUserProfile(
                        firstName = "User",
                        lastName = "",
                        username = "user@example.com",
                        profilePicture = null,
                        onComplete
                    )
                } else {
                    _error.value = errorMessage ?: ERROR_GOOGLE_SIGNIN_FAILED
                    onComplete(false)
                }
            }
        }
    }

    /**
     * Updates user registration status in preferences.
     */
    fun updateUserRegistrationStatus(context: Context) {
        UserPreferences(context).setUserRegistered(true)
    }

    // State update functions
    fun updateName(newName: String) { _name.value = newName }
    fun updateEmail(newEmail: String) { _email.value = newEmail }
    fun updatePassword(newPassword: String) { _password.value = newPassword }
    fun setError(error: String) { _error.value = error }
}
