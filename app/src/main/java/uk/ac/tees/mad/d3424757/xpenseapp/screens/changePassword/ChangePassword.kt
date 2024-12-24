package uk.ac.tees.mad.d3424757.xpenseapp.screens.changePassword

import XTopBar
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.XInputField
import uk.ac.tees.mad.d3424757.xpenseapp.components.XTitle
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.UserProfileVM

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navController: NavController,
    viewModel: UserProfileVM,
    modifier: Modifier = Modifier
) {
    val currentPassword by viewModel.currentPassword
    val newPassword by viewModel.newPassword
    val confirmPassword by viewModel.confirmPassword
    val errorMessage by viewModel.errorMessage

    Scaffold(
        topBar = {
            XTopBar(
                modifier = modifier,
                text = stringResource(R.string.profile_information),
                backClick = { navController.popBackStack() },
                textColor = Color.Black
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(mintCream)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            XTitle(color = tealGreen)

            // Password Fields
            PasswordField(label = stringResource(R.string.current_password), value = currentPassword) {
                viewModel.onCurrentPasswordChanged(it)
            }
            PasswordField(label = stringResource(R.string.new_password), value = newPassword) {
                viewModel.onNewPasswordChanged(it)
            }
            PasswordField(label = stringResource(R.string.confirm_password), value = confirmPassword) {
                viewModel.onConfirmPasswordChanged(it)
            }

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            XButton(
                handleClick = {
                    viewModel.onPasswordChange()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.change_password)
            )
        }
    }
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        /*--------------------Password Field-------------------*/
        XInputField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.password),
                    contentDescription = "password"
                )
            },
            keyboardType = KeyboardType.Password,
            isPassword = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)

        )

    }
}


