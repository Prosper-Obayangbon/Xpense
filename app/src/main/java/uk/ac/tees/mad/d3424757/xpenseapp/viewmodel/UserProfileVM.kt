package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.UserProfile
import uk.ac.tees.mad.d3424757.xpenseapp.repository.TransactionRepository
import uk.ac.tees.mad.d3424757.xpenseapp.repository.UserProfileRepository

class UserProfileVM(context : Context) : ViewModel() {
    private val repository: UserProfileRepository

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> get() = _userProfile


    // Initialize the repository with the DAO from the database.
    init {
        val dao = XpenseDatabase.getDatabase(context)
        repository = UserProfileRepository(dao)
        loadUserProfile(0)
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

    // Function to update user profile
    fun updateUserProfile(name: String, email: String) {
        viewModelScope.launch {
            // Update the profile and save changes (replace with actual update logic)
            val updatedProfile = _userProfile.value?.copy(name = name, email = email)
            _userProfile.value = updatedProfile
            if (updatedProfile != null) {
                saveUserProfile(updatedProfile)
            }
        }
    }

    private fun saveUserProfile(updatedProfile: UserProfile) {
        // Replace with logic to save the updated profile to your database or backend
    }

}