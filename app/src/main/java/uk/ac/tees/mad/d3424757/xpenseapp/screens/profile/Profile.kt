import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.components.BottomNavigationBar
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.UserProfileVM

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navController: NavController,
    userId: Int,
    viewModel: UserProfileVM,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showImagePickerDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mintCream)
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Profile Picture Section
                ProfilePictureSection(
                    profilePicture = userProfile?.profilePicture,
                    defaultProfilePicture = R.drawable.default_profile,
                    onChangePicture = { showImagePickerDialog = true }
                )


                Spacer(modifier = Modifier.height(32.dp))

                // User Information Section
                UserInfoSection(userProfile?.name, userProfile?.email)

                Spacer(modifier = Modifier.height(32.dp))

                // Profile Options
                ProfileOptionItem(icon = Icons.Default.AccountCircle, label = "Account Info") {
                    navController.navigate("profileInfo")
                }
                ProfileOptionItem(icon = Icons.Default.Lock, label = "Change Password") {
                    navController.navigate("change_password_screen")
                }
                ProfileOptionItem(Icons.Default.ExitToApp, label = "Logout") {
                    showLogoutDialog = true
                }

                // Logout Confirmation Dialog
                if (showLogoutDialog) {
                    LogoutConfirmationDialog(
                        onConfirm = {
                            showLogoutDialog = false
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate("login") {
                                popUpTo(0) // Clear navigation stack
                            }
                        },
                        onDismiss = { showLogoutDialog = false }
                    )
                }

                // Show Image Picker Dialog
                if (showImagePickerDialog) {
                    ImagePickerDialog(
                        onImagePicked = { uri ->
                            viewModel.saveProfilePicture(userId, uri.toString()) // Save the new profile picture
                            showImagePickerDialog = false
                        },
                        onDismiss = { showImagePickerDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
fun ProfilePictureSection(
    profilePicture: String?, // URI as String or null
    defaultProfilePicture: Int, // Resource ID for fallback
    onChangePicture: () -> Unit
) {
    val painter = if (profilePicture.isNullOrEmpty()) {
        painterResource(id = defaultProfilePicture) // Use default drawable
    } else {
        rememberAsyncImagePainter(model = profilePicture) // Use URI
    }

    Box(contentAlignment = Alignment.Center) {
        // Display the profile picture
        Image(
            painter = painter,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        // Add an icon to change the profile picture
        Icon(
            painter = painterResource(R.drawable.add_a_photo),
            contentDescription = "Change Picture",
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .clickable { onChangePicture() }
        )
    }
}


@Composable
fun UserInfoSection(name: String?, email: String?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = name ?: "Loading...",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black
        )
        Text(
            text = email ?: "@loading",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun LogoutConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Confirm Logout") },
        text = { Text(text = "Are you sure you want to log out?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

@Composable
fun ProfileOptionItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    ProfileScreen(navController = navController, userId = 0, viewModel = UserProfileVM(LocalContext.current))
}
