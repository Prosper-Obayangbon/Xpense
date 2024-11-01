package uk.ac.tees.mad.d3424757.xpenseapp.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen


@Composable
fun SplashScreen(navController: NavController){

    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    LaunchedEffect(key1 = true, block = {
        scale.animateTo(targetValue = 0.9f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(8f)
                        .getInterpolation(it)
                }
            ))

        delay(2000L)
        navController.navigate(XpenseScreens.Onboarding.name)
    })


    Surface(
        modifier = Modifier.padding(15.dp)
            .size(330.dp)
            .scale(scale.value),
        shape = CircleShape,
        color = tealGreen,
        border = BorderStroke(width = 2.dp, color = Color.LightGray)
    ) {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Row(
                modifier = Modifier.padding(1.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {

                Text(text ="X",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.lavishly)),
                        fontSize = 96.sp,
                        color = Color.White
                    )
                )
                Text(text = "PENSE",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.iowan)),
                        fontSize = 32.sp,
                        color = Color.White
                    )

                )
            }
            Text(
                text = "Track, Save, Grow...",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                color = Color.LightGray

                )
        }

    }
}
