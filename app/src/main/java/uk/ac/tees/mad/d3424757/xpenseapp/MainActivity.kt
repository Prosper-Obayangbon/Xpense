package uk.ac.tees.mad.d3424757.xpenseapp

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseNavigation
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme


class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted){
            Log.i("kilo","Permission granted")
        } else {
            Log.i("kilo", "Permission denied")
        }

    }

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

    private fun requestCameraPermission(){
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("kilo", "Permission previously granted")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> Log.i("kilo", "Show camera permissions dialog")

            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun XpenseApp(modifier: Modifier = Modifier, context : Context){
    XpenseNavigation(modifier, context)

}
