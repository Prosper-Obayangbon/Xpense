package uk.ac.tees.mad.d3424757.xpenseapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.size.Dimension
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream

@Composable
fun XHomeBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()){
            val(topBar, contentArea) = createRefs()
            Image(painter = painterResource(
                id = R.drawable.ic_top),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(topBar){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })

            // Content Area for all composable
            Box(
                modifier = Modifier
                    .constrainAs(contentArea) {
                        top.linkTo(topBar.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                content()
            }

        }
    }
}