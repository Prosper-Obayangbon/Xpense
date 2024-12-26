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
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_GOOGLE_SIGNIN_FAILED
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_INCORRECT_CREDENTIALS
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_NAME_EMPTY
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_PASSWORD_COMPLEXITY
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_PASSWORD_EMPTY
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_SAVE_PROFILE_FAILED
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ERROR_SIGNUP_FAILED
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.PASSWORD_PATTERN

class AuthViewModel(context: Context) : ViewModel() {
    private val authRepository = AuthRepository()
    val dao = XpenseDatabase.getDatabase(context)
    val repository = UserProfileRepository(dao)

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


    /**
     * Initiates the sign-up process if all validation checks pass.
     * @param name The name of the user
     * @param username The username of the user
     * @param profilePicture The profile picture URI (can be null)
     * @param onComplete Callback indicating the success or failure of sign-up
     */
    fun signUpUser(onComplete: (Boolean) -> Unit) {
        validateInputs()?.let { errorMessage ->
            _error.value = errorMessage
            onComplete(false)
        } ?: run {
            executeSignUp(name, email, "", onComplete)
        }
    }

    /**
     * Validates user inputs and returns an error message if validation fails.
     * @return Error message or null if validation is successful
     */
    private fun validateInputs(): String? {
        return when {
            name.isBlank() -> ERROR_NAME_EMPTY
            email.isBlank() -> ERROR_EMAIL_EMPTY
            password.isBlank() -> ERROR_PASSWORD_EMPTY
            !password.matches(PASSWORD_PATTERN) -> ERROR_PASSWORD_COMPLEXITY
            else -> null
        }
    }

    /**
     * Executes the sign-up process by calling AuthRepository and saving user profile to the database.
     * @param name The name of the user
     * @param username The username of the user
     * @param profilePicture The profile picture URI (can be null)
     * @param onComplete Callback indicating the success or failure of the sign-up
     */
    private fun executeSignUp(name: String, username: String, profilePicture: String?, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            // Perform sign-up with the AuthRepository
            authRepository.signUpWithEmail(email, password) { success, errorMessage ->
                if (success) {
                    // Clear any existing errors
                    _error.value = null

                    // After sign-up is successful, create the UserProfile
                    val userId = authRepository.getCurrentUserId() // Get the user ID after successful sign-up
                    val userProfile = UserProfile(
                        name = name,
                        email = username,
                        profilePicture = profilePicture ?: ""
                    )

                    // Save the user profile to the database
                    try {
                        saveUserProfileToDatabase(userProfile) // Now inside the coroutine
                        // Return success
                        onComplete(true)
                    } catch (e: Exception) {
                        // If there is an error saving the profile, return failure
                        _error.value = "$ERROR_SAVE_PROFILE_FAILED ${e.message}"
                        onComplete(false)
                    }
                } else {
                    // If sign-up failed, set the error message and return failure
                    _error.value = errorMessage ?: ERROR_SIGNUP_FAILED
                    onComplete(false)
                }
            }
        }
    }


    /**
     * Save the user profile to the database.
     * @param userProfile The user profile to save
     */
    private fun saveUserProfileToDatabase(userProfile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(userProfile)
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
                    _error.value = ERROR_INCORRECT_CREDENTIALS
                    onComplete(false)
                }
            }
        }
    }

    // Function to handle Google Sign-In
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
                    onComplete(true)
                } else {
                    _error.value = errorMessage ?: ERROR_GOOGLE_SIGNIN_FAILED
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
