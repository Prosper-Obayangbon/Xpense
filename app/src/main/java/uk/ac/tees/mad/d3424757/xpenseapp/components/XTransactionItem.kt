package uk.ac.tees.mad.d3424757.xpenseapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream

@Composable
fun TransactionItem(itemName : String, description: String, amount: String, time: String, icon :Int, iconColor: Color){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp, start = 16.dp, end = 16.dp)
            .size(70.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(iconColor,
                    shape = RoundedCornerShape(12.dp)
                )
        ){
            Icon(
                painter  = painterResource(id= icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.align(Alignment.Center)

            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)){
            Text(text = itemName, fontWeight = FontWeight.Bold)
            Text(text = description, color = Color.Gray, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = amount, color = if(amount.contains("-")) Color.Red else Color.Green, fontWeight = FontWeight.Bold)
            Text(text = time, color = Color.Gray, fontSize = 12.sp)
        }
    }
}
@Preview
@Composable
fun InputPreview(){
        XpenseAppTheme{
          TransactionItem("Shopping","for food","-55", "10pm", icon = R.drawable.ic_google_icon, Color.Blue)
        }

}