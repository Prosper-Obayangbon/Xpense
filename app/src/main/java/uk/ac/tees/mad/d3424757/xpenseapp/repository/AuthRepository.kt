package uk.ac.tees.mad.d3424757.xpenseapp.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Sign up a user with email and password.
     */
    fun signUpWithEmail(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    /**
     * Sign in a user with email and password.
     */
    fun signInWithEmail(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    /**
     * Sign in a user with Google credentials using the provided ID token.
     */
    fun signInWithGoogle(idToken: String, onComplete: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    /**
     * Gets the current authenticated user's ID.
     * Can be useful for saving user profile information.
     */
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    /**
     * Reauthenticate the user with their email and current password.
     */
    fun reauthenticateUser(
        email: String,
        currentPassword: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val credential: AuthCredential = EmailAuthProvider.getCredential(email, currentPassword)
        auth.currentUser?.reauthenticate(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    /**
     * Change the authenticated user's password.
     */
    fun changePassword(
        currentPassword: String,
        newPassword: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val user = auth.currentUser
        val email = user?.email

        if (email != null) {
            // Reauthenticate first
            reauthenticateUser(email, currentPassword) { success, error ->
                if (success) {
                    // Update the password
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onComplete(true, null)
                            } else {
                                onComplete(false, task.exception?.message)
                            }
                        }
                } else {
                    onComplete(false, error)
                }
            }
        } else {
            onComplete(false, "User email not found.")
        }
    }
}
