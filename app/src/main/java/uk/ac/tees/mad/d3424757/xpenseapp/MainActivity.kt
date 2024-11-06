package uk.ac.tees.mad.d3424757.xpenseapp

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseNavigation
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.repository.AuthRepository
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.AuthViewModel
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.SignViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var signViewModel:SignViewModel
    private lateinit var navController: NavHostController // Store the navController as a property


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        class MyApplication : Application() {
            override fun onCreate() {
                super.onCreate()

                signViewModel = SignViewModel()

                FirebaseApp.initializeApp(this)
            }
        }

        setContent {
            XpenseAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()

                    XpenseApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {


        if (task.isSuccessful) {
            val account = task.result
            account?.idToken?.let { idToken ->
                signViewModel.executeGoogleSignIn(idToken) { success ->
                    if (success) {
                        navController.navigate(XpenseScreens.Home.name)
                    } else {
                        // Handle error
                    }
                }
            }
        } else {
            // Handle error
        }
    }
}

@Composable
fun XpenseApp(modifier: Modifier = Modifier){

    Surface(modifier.fillMaxSize(),
        color = tealGreen
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            XpenseNavigation()
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    XpenseAppTheme {
        XpenseApp()
    }
}