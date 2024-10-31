package uk.ac.tees.mad.d3424757.xpenseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseNavigation
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XpenseAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    XpenseApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun XpenseApp(modifier: Modifier = Modifier){

    Surface(modifier.fillMaxSize()) {
        XpenseNavigation()
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    XpenseAppTheme {
        XpenseApp()
    }
}