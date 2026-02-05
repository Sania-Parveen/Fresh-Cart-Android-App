package com.example.freshcartapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CongratulationOrderSuccessful(
    onHomeButtonClicked: () -> Unit,
    onContinueShopping: () -> Unit,
) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF9F9F9)) // Light gray background
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFDDDDDD), RoundedCornerShape(16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Success Icon
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Order Successful",
                tint = Color(123, 187, 180), // Green color
                modifier = Modifier
                    .size(90.dp)
                    .background(Color(0xFFe8f5e9), shape = RoundedCornerShape(50.dp)) // Light green background
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Success Message
            Text(
                text = "Congratulations!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your order has been placed successfully.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Continue Shopping Button
            Button(
                onClick = onContinueShopping,
                colors = ButtonDefaults.buttonColors(containerColor = Color(123, 187, 180)), // Green color
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Continue Shopping",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}
