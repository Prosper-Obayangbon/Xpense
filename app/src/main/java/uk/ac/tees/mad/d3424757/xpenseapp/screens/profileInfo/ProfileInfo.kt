package uk.ac.tees.mad.d3424757.xpenseapp.screens.profileInfo

import XTopBar
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.XInputField
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.UserProfile
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.UserProfileVM

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileInfoScreen(
    navController: NavController,
    viewModel: UserProfileVM,
    modifier: Modifier = Modifier
) {
    // Observe the user profile from the ViewModel
    val userProfile by viewModel.userProfile.collectAsState()

    // Mutable states for editable fields
    var name = userProfile?.name
    var email = userProfile?.email

    Scaffold(
        topBar = {
            XTopBar(modifier = modifier,
                text = "Profile Information",
                backClick = {navController.popBackStack()},
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
            Spacer(modifier = Modifier.height(150.dp))

            // Profile Picture
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = rememberAsyncImagePainter(model = userProfile?.profilePicture),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Editable Fields
            name?.let { EditableField(label = "Name", value = it, onValueChange = { name = it }, type = "Name") }
            email?.let { EditableField(label = "Email", value = it, onValueChange = { email = it }, type = "Email") }

            Spacer(modifier = Modifier.height(20.dp))

            // Save Button
            XButton(
                handleClick = {
                    // Call the ViewModel to save the updated profile
                    name?.let { email?.let { it1 -> viewModel.updateUserProfile(it, it1) } }
                },
                modifier = Modifier.fillMaxWidth(),
                text = "Save Changes"
            )
        }
    }
}

@Composable
fun EditableField(
    label: String,
    value: String,
    type : String,
    onValueChange: (String) -> Unit
) {
// Determine the keyboard type based on the field type
    val keyboardType = when (type) {
        "Email" -> KeyboardType.Email
        "Name" -> KeyboardType.Text
        else -> KeyboardType.Unspecified // Default for other types
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        XInputField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                when (type) {
                    "Email" -> Icon(Icons.Default.Email, contentDescription = "Email Icon")
                    "Name" -> Icon(Icons.Default.Person, contentDescription = "Name Icon")
                    else -> null
                }
            },
            keyboardType = keyboardType
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileInfoScreenPreview() {
    val navController = rememberNavController()
    val mockViewModel = UserProfileVM(LocalContext.current)
    ProfileInfoScreen(navController = navController, viewModel = mockViewModel)
}




