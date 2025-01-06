package uk.ac.tees.mad.d3424757.xpenseapp.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthRepository {

    // FirebaseAuth instance to interact with Firebase Authentication
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Sign up a user with email and password.
     * Creates a new user account.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @param onComplete Callback that returns whether the operation was successful and any error message if applicable.
     */
    fun signUpWithEmail(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // User successfully signed up
                } else {
                    onComplete(false, task.exception?.message) // Error during sign-up
                }
            }
    }

    /**
     * Sign in a user with email and password.
     * Authenticates an existing user with the given credentials.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @param onComplete Callback that returns whether the operation was successful and any error message if applicable.
     */
    fun signInWithEmail(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // User successfully signed in
                } else {
                    onComplete(false, task.exception?.message) // Error during sign-in
                }
            }
    }

    /**
     * Sign in a user with Google credentials using the provided ID token.
     * Useful for integrating Google Sign-In.
     *
     * @param idToken The ID token received from Google Sign-In.
     * @param onComplete Callback that returns whether the operation was successful and any error message if applicable.
     */
    fun signInWithGoogle(idToken: String, onComplete: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // User successfully signed in
                } else {
                    onComplete(false, task.exception?.message) // Error during Google sign-in
                }
            }
    }

    /**
     * Gets the current authenticated user's ID.
     * Can be useful for saving user profile information or interacting with user data.
     *
     * @return The current user's UID or null if no user is authenticated.
     */
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    /**
     * Reauthenticate the user with their email and current password.
     * Used for sensitive actions like password change.
     *
     * @param email The user's email address.
     * @param currentPassword The user's current password.
     * @param onComplete Callback that returns whether the operation was successful and any error message if applicable.
     */
    private fun reauthenticateUser(
        email: String,
        currentPassword: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val credential: AuthCredential = EmailAuthProvider.getCredential(email, currentPassword)
        auth.currentUser?.reauthenticate(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // Reauthentication successful
                } else {
                    onComplete(false, task.exception?.message) // Error during reauthentication
                }
            }
    }

    /**
     * Change the authenticated user's password.
     * Requires reauthentication before changing the password.
     *
     * @param currentPassword The current password of the user.
     * @param newPassword The new password to be set.
     * @param onComplete Callback that returns whether the operation was successful and any error message if applicable.
     */
    fun changePassword(
        currentPassword: String,
        newPassword: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val user = auth.currentUser
        val email = user?.email

        if (email != null) {
            // Reauthenticate first to ensure the user is authenticated before changing password
            reauthenticateUser(email, currentPassword) { success, error ->
                if (success) {
                    // Update the password after reauthentication
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onComplete(true, null) // Password successfully changed
                            } else {
                                onComplete(false, task.exception?.message) // Error during password change
                            }
                        }
                } else {
                    onComplete(false, error) // Reauthentication failed
                }
            }
        } else {
            onComplete(false, "User email not found.") // No email found for the user
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
