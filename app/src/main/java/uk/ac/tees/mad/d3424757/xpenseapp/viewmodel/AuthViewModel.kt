package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel

import androidx.lifecycle.ViewModel
import uk.ac.tees.mad.d3424757.xpenseapp.repository.AuthRepository

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    fun signUpUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        authRepository.signUpWithEmail(email, password, onComplete)
    }

    fun signInUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        authRepository.signInWithEmail(email, password, onComplete)
    }
}