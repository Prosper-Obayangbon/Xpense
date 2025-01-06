package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.UserProfile
import uk.ac.tees.mad.d3424757.xpenseapp.repository.AuthRepository
import uk.ac.tees.mad.d3424757.xpenseapp.repository.UserProfileRepository
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.PROFILE_LOAD_ERROR

/**
 * ViewModel for managing the user profile and authentication-related operations.
 */
class UserProfileVM(
    context: Context,
    private val showToast: (String) -> Unit // Callback to show toast messages
) : ViewModel() {

    private val repository: UserProfileRepository
    private val authRepository: AuthRepository = AuthRepository()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> get() = _userProfile

    private val _currentPassword = mutableStateOf("")
    val currentPassword: State<String> get() = _currentPassword

    private val _newPassword = mutableStateOf("")
    val newPassword: State<String> get() = _newPassword

    private val _confirmPassword = mutableStateOf("")
    val confirmPassword: State<String> get() = _confirmPassword

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> get() = _errorMessage

    init {
        val dao = XpenseDatabase.getDatabase(context)
        repository = UserProfileRepository(dao)


        viewModelScope.launch {
            try {
                Log.d("UserProfileVM", "Fetching user profile...")
                _userProfile.value = repository.getUserProfile()
                Log.d("UserProfileVM", "User profile loaded successfully: ${_userProfile.value}")
            } catch (e: Exception) {
                _userProfile.value = null
                Log.e("UserProfileVM", "Error fetching user profile", e)
                showToast(PROFILE_LOAD_ERROR)
            }
        }
    }

    /**
     * Loads the user profile based on the user ID.
     * @param userId The ID of the user whose profile needs to be loaded.
     */
    private fun loadUserProfile() {

    }

    /**
     * Saves the profile picture URI for a user.
     * @param uri The URI of the new profile picture.
     */
    fun saveProfilePicture(uri: String) {
        viewModelScope.launch {
            try {
                repository.updateProfilePicture(uri)
                _userProfile.value = repository.getUserProfile() // Explicitly refresh profile
                showToast(Constants.PROFILE_UPDATE_SUCCESS)
            } catch (e: Exception) {
                Log.e("UserProfileVM", "Error updating profile picture", e)
                showToast(Constants.PROFILE_PICTURE_UPDATE_ERROR)
            }
        }
    }


    /**
     * Updates the user profile with the provided name and email.
     * @param name The new name for the user.
     * @param email The new email for the user.
     */
    fun updateUserProfile(name: String, email: String) {
        viewModelScope.launch {
            val nameParts = name.trim().split(" ")
            val updatedProfile = _userProfile.value?.copy(firstName = nameParts.first(),
                lastName = nameParts.drop(1).joinToString(" "), email = email)
            updatedProfile?.let {
                try {
                    repository.updateUserProfile(it) // Update profile in the repository
                    _userProfile.value = it // Update the user profile in the state
                    showToast(Constants.PROFILE_UPDATE_SUCCESS)
                } catch (e: Exception) {
                    showToast(Constants.PROFILE_PICTURE_UPDATE_ERROR)
                }
            }
        }
    }

    /**
     * Updates the current password state when the user changes the current password.
     * @param password The new current password.
     */
    fun onCurrentPasswordChanged(password: String) {
        _currentPassword.value = password
    }

    /**
     * Updates the new password state when the user enters a new password.
     * @param password The new password.
     */
    fun onNewPasswordChanged(password: String) {
        _newPassword.value = password
    }

    /**
     * Updates the confirm password state when the user confirms the new password.
     * @param password The confirmed new password.
     */
    fun onConfirmPasswordChanged(password: String) {
        _confirmPassword.value = password
    }

    /**
     * Handles the password change process by validating the new and confirm passwords,
     * then calling the repository to change the password.
     */
    fun onPasswordChange() {
        // Check if the new password and confirm password match
        if (_newPassword.value == _confirmPassword.value) {
            _errorMessage.value = "" // Clear any previous error messages

            viewModelScope.launch {
                try {
                    // Call the authRepository to change the password
                    authRepository.changePassword(
                        currentPassword = _currentPassword.value,
                        newPassword = _newPassword.value
                    ) { success, error ->
                        // Handle success or error and show the respective toast message
                        if (success) {
                            showToast(Constants.PASSWORD_CHANGE_SUCCESS)
                        } else {
                            showToast(error ?: Constants.PASSWORD_CHANGE_ERROR)
                        }
                    }
                } catch (e: Exception) {
                    showToast(Constants.PASSWORD_CHANGE_ERROR)
                }
            }
        } else {
            _errorMessage.value = Constants.PASSWORD_MISMATCH_ERROR
        }
    }
}
