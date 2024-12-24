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

    // Regular expression for password complexity requirements
    private val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")

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
            name.isBlank() -> "Name cannot be empty."
            email.isBlank() -> "Email cannot be empty."
            password.isBlank() -> "Password cannot be empty."
            !password.matches(passwordPattern) -> "Password must contain at least 8 characters, including uppercase, lowercase, number, and symbol."
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
                        _error.value = "Error saving user profile: ${e.message}"
                        onComplete(false)
                    }
                } else {
                    // If sign-up failed, set the error message and return failure
                    _error.value = errorMessage ?: "Sign-up failed. Please try again."
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
            repository.saveUserProfile(userProfile)        }


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
                    _error.value = "Incorrect Email or Password."
                    onComplete(false)
                }
            }
        }
    }

    // Function to handle Google Sign-In
    fun executeGoogleSignIn(idToken: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            authRepository.signInWithGoogle(idToken) { success, errorMessage ->
                if (success) {
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
