package uk.ac.tees.mad.d3424757.xpenseapp.screens.add

import XTopBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.d3424757.xpenseapp.components.RecentTransactions
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.HomeViewModel


@Composable
fun AddScreen(
    modifier: Modifier,
    viewModel: HomeViewModel,
    navController: NavController
) {

    // Observe transactions from the ViewModel
    val transactions by viewModel.transactions.collectAsState(emptyList())
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding()
            .background(mintCream)
    ) {
        XTopBar(
            modifier = Modifier.fillMaxWidth(),
            text = "Add",
            textColor = Color.Black,
            backClick = {navController.popBackStack()}
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AddButton(
                label = "Add Income",
                backgroundColor = Color(0xFFB2D9D2),
                iconColor = Color(0xFF8C65F7),
                navController = navController
            )
            AddButton(
                label = "Add Expense",
                backgroundColor = Color(0xFFFFE6E6),
                iconColor = Color(0xFF8C65F7),
                navController = navController
            )
        }


        Text(
            text = "Last Added",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        RecentTransactions(transactions = transactions)
    }
}






@Composable
fun AddButton(label: String, backgroundColor: Color, iconColor: Color, navController: NavController) {
    Box(
        modifier = Modifier
            .width(185.dp)
            .height(115.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(tealGreen)
            .clickable {navController.navigate(XpenseScreens.AddTransaction.route)  },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Email, // Replace with appropriate icon
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(50.dp)
            )
            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}







@Preview
@Composable
fun AddPreview(){
    XpenseAppTheme{
        //AddScreen(viewModel = HomeViewModel(LocalContext.current))
    }
}
