import android.annotation.SuppressLint
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.components.BottomNavigationBar
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavController, modifier: Modifier) {

    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
        // Bottom navigation
        BottomNavigationBar(navController = navController)
    },
    ) {
        Box(modifier = Modifier.fillMaxSize()
            .background(mintCream)
        ){
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.default_profile),
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(100.dp).clip(CircleShape).clickable { }//onUploadPicture() }
                        )
                        Icon(
                            painter = painterResource(R.drawable.add_a_photo),
                            contentDescription = "Change Picture",
                            modifier = Modifier.size(40.dp).align(Alignment.BottomEnd).padding(8.dp)
                        )
                    }
                    // Profile Section (Image + Name + Email in Row)


                    Spacer(modifier = Modifier.width(16.dp))

                    // Name and Email
                    Column(
                        modifier = Modifier.padding(top = 20.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Enjelin Morgeana",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black
                        )
                        Text(
                            text = "@enjelin_morgeana",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }



                Spacer(modifier = Modifier.height(32.dp))


                // Options List
                ProfileOptionItem(icon = Icons.Default.AccountCircle, label = "Account Info") {
                    // Navigate to Account Info
                }
                ProfileOptionItem(icon = Icons.Default.Lock, label = "Change Password") {
                    navController.navigate("change_password_screen")
                }
                ProfileOptionItem(Icons.Default.ExitToApp, label = "Logout"){
                    showLogoutDialog = true
                }

                // Logout Confirmation Dialog
                if (showLogoutDialog) {
                    AlertDialog(
                        onDismissRequest = { showLogoutDialog = false }, // Dismiss the dialog
                        title = { Text(text = "Confirm Logout") },
                        text = { Text(text = "Are you sure you want to log out?") },
                        confirmButton = {
                            TextButton(onClick = {
                                showLogoutDialog = false
                                FirebaseAuth.getInstance().signOut()
                                navController.navigate("login") {
                                    popUpTo(0) // Clear navigation stack
                                }
                            }) {
                                Text("Yes", color = Color.Red)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showLogoutDialog = false }) {
                                Text("No")
                            }
                        }
                    )
                }



            }
        }

    }
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
    // Remember a NavController for preview purposes
    val navController = rememberNavController()

    // Call the actual ProfileScreen composable
    ProfileScreen(navController = navController, modifier = Modifier)
}
