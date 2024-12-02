package uk.ac.tees.mad.d3424757.xpenseapp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseNavigation
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.repository.AuthRepository
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.AuthViewModel
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.SignViewModel
import kotlin.text.Typography.dagger


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        class MyApplication : Application() {
            override fun onCreate() {
                super.onCreate()
                FirebaseApp.initializeApp(this)
            }
        }
        setContent {
            XpenseAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()) { innerPadding ->

                    XpenseApp(modifier = Modifier.padding(innerPadding), this)
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun XpenseApp(modifier: Modifier = Modifier, context : Context){
    XpenseNavigation(modifier, context)

}
