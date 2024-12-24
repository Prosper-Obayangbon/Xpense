package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.UserProfile
import uk.ac.tees.mad.d3424757.xpenseapp.repository.AuthRepository
import uk.ac.tees.mad.d3424757.xpenseapp.repository.UserProfileRepository
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants

class UserProfileVM(
    context: Context,
    private val showToast: (String) -> Unit // Callback to show toast messages
) : ViewModel() {

    val dao = XpenseDatabase.getDatabase(context)
    private val repository: UserProfileRepository = UserProfileRepository(dao)
    private val authRepository: AuthRepository = AuthRepository() // Initialize with necessary dependencies if any

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
        loadUserProfile(0) // Load a default profile (you can pass an actual user ID)
    }

    private fun loadUserProfile(userId: Int) {
        viewModelScope.launch {
            _userProfile.value = repository.getUserProfile(userId)
        }
    }

    fun saveProfilePicture(userId: Int, uri: String) {
        viewModelScope.launch {
            repository.updateProfilePicture(userId, uri)
            loadUserProfile(userId) // Refresh user profile after update
        }
    }

    fun updateUserProfile(name: String, email: String) {
        viewModelScope.launch {
            val updatedProfile = _userProfile.value?.copy(name = name, email = email)
            _userProfile.value = updatedProfile
            updatedProfile?.let {
                repository.updateUserProfile(it)
                showToast("Update successfully!")

            }
        }
    }

    // Functions to update password state
    fun onCurrentPasswordChanged(password: String) {
        _currentPassword.value = password
    }

    fun onNewPasswordChanged(password: String) {
        _newPassword.value = password
    }

    fun onConfirmPasswordChanged(password: String) {
        _confirmPassword.value = password
    }

    // Password change logic
    fun onPasswordChange() {
        if (_newPassword.value == _confirmPassword.value) {
            _errorMessage.value = ""

            viewModelScope.launch {
                authRepository.changePassword(
                    currentPassword = _currentPassword.value,
                    newPassword = _newPassword.value
                ) { success, error ->
                    if (success) {
                        showToast("Password changed successfully!")
                    } else {
                        showToast("Error: $error")
                    }
                }
            }
        } else {
            _errorMessage.value = "Passwords do not match"
        }
    }
}
