package uk.ac.tees.mad.d3424757.xpenseapp.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import uk.ac.tees.mad.d3424757.xpenseapp.components.BottomNavigationBar
import uk.ac.tees.mad.d3424757.xpenseapp.components.TransactionItem
import uk.ac.tees.mad.d3424757.xpenseapp.components.XHomeBackground
import uk.ac.tees.mad.d3424757.xpenseapp.data.preferences.TransactionData
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseNavigation
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen

@Composable
fun Home(){

    // Background composable with all Home content
    XHomeBackground() {
        Column{
            Box(modifier = Modifier.size(790.dp)){
                Column {
                    HeaderSection()
                    BalanceCard()
                    RecentTransactions()
                }
            }
            BottomNavigationBar()

        }

    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Column {
            Text(text = "Good Morning", fontSize = 16.sp, color = Color.White)
            Text(
                text = "Prosper",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color =Color.White
            )

        }
        Spacer(modifier = Modifier.weight(1f))

        // Notification icon button
        IconButton(
            onClick = {},
            modifier = Modifier.align(Alignment.CenterVertically)

        ) {
            Icon(
                painter = painterResource(id = R.drawable.notification),
                contentDescription = "Username Icon",
                tint = Color.White,
            )
        }
    }

}

@Composable
fun BalanceCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(tealGreen),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(200.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Total Balance", color = Color.White, fontSize = 14.sp)
                    Text(
                        text = "£2,548.00",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.more_horiz),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CardRowItem("Income", "£1,840.00", R.drawable.arrow_downward)
                CardRowItem("Expenses", "£284.00", R.drawable.arrow_upward)
            }
        }
    }
}

@Composable
fun CardRowItem(title: String, amount: String, image: Int){
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color.White.copy(alpha = 0.2f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, color = Color.White, fontSize = 16.sp)
        }
        Text(
            text = amount,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}


@Composable
fun RecentTransactions() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal =16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Recent Transaction", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Button(onClick = {},
                colors = ButtonDefaults.buttonColors(mintCream),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)) {
                Text("See All", color = tealGreen)
            }
        }


        // Sample transaction items
        val transactions = listOf(
            //TransactionData(name = "Shopping", description = "Buy some grocery", amount = "- $120", time = "10:00 AM", image = R.drawable.ic_google_icon, iconColor = Color(0xFFFFD2C4)),
            //TransactionData(name = "Subscription", description = "Disney+ Annual..", amount = "- $80", time = "03:30 PM", image = R.drawable.ic_google_icon, iconColor = Color(0xFFD7DEFF)),
            //TransactionData(name = "Food", description = "Buy a ramen", amount = "- $32", time = "07:30 PM", image = R.drawable.ic_google_icon, iconColor = Color(0xFFFFC8C7)),
            //TransactionData(name = "Salary", description = "Salary for July", amount = "+ $5000", time = "04:30 PM", image = R.drawable.ic_google_icon, iconColor = Color(0xFFDEF7E7)),
            TransactionData(name = "Salary", description = "Salary for July", amount = "+ $5000", time = "04:30 PM", image = R.drawable.ic_google_icon, iconColor = Color(0xFFDEF7E7)),
            TransactionData(name = "Salary", description = "Salary for July", amount = "+ $5000", time = "04:30 PM", image = R.drawable.ic_google_icon, iconColor = Color(0xFFDEF7E7))
        )
        // Display each transaction item

            LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ){
            items(transactions){ transaction ->
                TransactionItem(
                    itemName = transaction.name,
                    description = transaction.description,
                    amount = transaction.amount,
                    time = transaction.time,
                    icon = transaction.image,
                    iconColor = transaction.iconColor
                )

            }
        }


    }

}


@Preview
@Composable
fun HomePreview(){
    XpenseAppTheme{
        Home()
    }
}
