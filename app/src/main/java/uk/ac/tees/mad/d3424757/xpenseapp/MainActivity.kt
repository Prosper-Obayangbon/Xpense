package uk.ac.tees.mad.d3424757.xpenseapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseNavigation
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme


class MainActivity : FragmentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            XpenseAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    XpenseApp(modifier = Modifier.padding(innerPadding), context = this)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun XpenseApp(modifier: Modifier = Modifier, context: Context) {
    XpenseNavigation(modifier, context)
}
