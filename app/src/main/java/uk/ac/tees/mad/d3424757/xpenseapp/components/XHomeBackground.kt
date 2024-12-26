package uk.ac.tees.mad.d3424757.xpenseapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream

/**
 * A composable that provides a background layout with a top bar image and a content area.
 * The content area is a placeholder for any composable that needs to be displayed on top of this background.
 *
 * @param modifier An optional modifier for styling the background or overriding default properties.
 * @param content A composable lambda to define the content that appears in the content area of the background.
 */
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

            Image(
                painter = painterResource(id = R.drawable.ic_top),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

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
