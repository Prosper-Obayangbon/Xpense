package uk.ac.tees.mad.d3424757.xpenseapp.screens.profile

import XTopBar
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.XInputField
import uk.ac.tees.mad.d3424757.xpenseapp.components.XTitle
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.UserProfileVM

/**
 * ChangePasswordScreen Composable:
 * This screen allows the user to change their password by entering the current password,
 * new password, and confirming the new password. It also provides error feedback and a button to save changes.
 *
 * @param navController Navigation controller to handle navigation.
 * @param viewModel ViewModel that manages the user's profile and password state.
 * @param modifier Modifier to customize the layout.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChangePasswordScreen(
    navController: NavController,
    viewModel: UserProfileVM,
    modifier: Modifier = Modifier
) {
    // State for the current, new, and confirm password fields, as well as error messages.
    val currentPassword by viewModel.currentPassword
    val newPassword by viewModel.newPassword
    val confirmPassword by viewModel.confirmPassword
    val errorMessage by viewModel.errorMessage

    Scaffold(
        topBar = {
            // Top bar with a title and back navigation
            XTopBar(
                modifier = modifier,
                text = stringResource(R.string.profile_information),
                backClick = { navController.popBackStack() },
                textColor = Color.Black
            )
        },
    ) {
        // Main content layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(mintCream) // Background color for the screen
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp)) // Add space at the top

            // Title at the top of the screen
            XTitle(color = tealGreen)

            // Password input fields for current, new, and confirm password
            PasswordField(label = stringResource(R.string.current_password), value = currentPassword) {
                viewModel.onCurrentPasswordChanged(it) // Update the current password state
            }
            PasswordField(label = stringResource(R.string.new_password), value = newPassword) {
                viewModel.onNewPasswordChanged(it) // Update the new password state
            }
            PasswordField(label = stringResource(R.string.confirm_password), value = confirmPassword) {
                viewModel.onConfirmPasswordChanged(it) // Update the confirm password state
            }

            // Display error message if there's any
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp)) // Add space before the button

            // Button to save the password changes
            XButton(
                handleClick = {
                    viewModel.onPasswordChange() // Handle password change
                    navController.popBackStack() // Navigate back to the previous screen
                },
                modifier = Modifier.fillMaxWidth(), // Make the button full-width
                text = stringResource(R.string.change_password)
            )
        }
    }
}

/**
 * PasswordField Composable:
 * This composable creates a password input field with a label and icon. It is used for inputting
 * the current password, new password, and confirm password.
 *
 * @param label The label to display above the password field.
 * @param value The current value of the password field.
 * @param onValueChange Callback to update the value of the password field.
 */
@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Label for the password field
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp)) // Space between label and input field

        // Password input field
        XInputField(
            value = value,
            onValueChange = onValueChange, // Update the value when the user types
            leadingIcon = {
                // Icon displayed on the left of the input field
                Icon(
                    painterResource(id = R.drawable.password),
                    contentDescription = "password"
                )
            },
            keyboardType = KeyboardType.Password, // Use password keyboard
            isPassword = true, // Indicates that this is a password field
            modifier = Modifier
                .fillMaxWidth() // Make the input field full width
                .padding(bottom = 16.dp) // Add padding below the field
        )
    }
}
