package com.example.freshcartapp.ui.theme


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CheckoutPage(
    freshViewModel: FreshViewModel,
    onOrderConfirmed: () -> Unit
) {
    val context = LocalContext.current
    val user by freshViewModel.user.collectAsState()

    val cartItems by freshViewModel.cartItems.collectAsState()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var apartment by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var deliveryOption by remember { mutableStateOf("Standard Delivery") }
    var paymentMethod by remember { mutableStateOf("Credit/Debit Card") }
    var giftMessage by remember { mutableStateOf("") }
    val totalPrice =  cartItems.sumOf { it.itemPrice * 75/100 }
    val handlingCharge = totalPrice*1/100
    val deliveryFee = 10
    val gst = totalPrice*2/100
    val grandTotal = totalPrice + handlingCharge + deliveryFee + gst

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(10.dp) // Add space between the list and form
    ) {

        // Order Summary
        Text(
            text = "Order Summary",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        OrderSummarySection(
            total = grandTotal
        )

        // Delivery Options
        Text(
            text = "Delivery Options",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        DeliveryOptionsSection(
            selectedOption = deliveryOption,
            onOptionSelected = { deliveryOption = it }
        )

        // Payment Methods
        Text(
            text = "Payment Methods",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        PaymentMethodsSection(
            selectedMethod = paymentMethod,
            onMethodSelected = { paymentMethod = it }
        )


        // Gift Message
        Text(
            text = "Gift Message",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = giftMessage,
            onValueChange = { giftMessage = it },
            label = { Text("Add a gift message") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium // Rounded corners
        )

        Text(
            text = "Order Details",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                top = 10.dp, bottom = 15.dp
            )
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
            ) {
                // First Name and Last Name Fields
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name") },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium // Rounded corners
                    )
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name") },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium // Rounded corners
                    )
                }

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium // Rounded corners
                )

                // Contact Field
                OutlinedTextField(
                    value = contact,
                    onValueChange = { contact = it },
                    label = { Text("Contact") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium // Rounded corners
                )

                // Address Field
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium // Rounded corners
                )

                // Apartment/Suite Field
                OutlinedTextField(
                    value = apartment,
                    onValueChange = { apartment = it },
                    label = { Text("Apartment, suite, etc.") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium // Rounded corners
                )

                // City Field
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium // Rounded corners
                )

                // ZIP/Postal Code Field
                OutlinedTextField(
                    value = zipCode,
                    onValueChange = { zipCode = it },
                    label = { Text("ZIP/Postal Code") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium // Rounded corners
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Order Now Button
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color(123, 187, 180)),
            onClick = {
                onOrderConfirmed()
                if (firstName.isNotBlank() && lastName.isNotBlank() && address.isNotBlank() && city.isNotBlank() && contact.isNotBlank() && zipCode.isNotBlank() && email.isNotBlank()
                ) {
                    // Handle order confirmation logic
                    freshViewModel.placeOrder(
                        username = "$firstName $lastName",
                        contactNumber = contact,
                        address = "$address, $apartment, $city, $zipCode",
                        email = email,
                        onSuccess = {

                        },
                        onFailure = {
                            Toast.makeText(context, "Order Placement Failed", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = MaterialTheme.shapes.medium // Rounded corners
        ) {
            Text(text = "Order Now", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

@Composable
fun DeliveryOptionsSection(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column {
        listOf("Standard Delivery", "Express Delivery").forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = option, fontSize = 16.sp)
                RadioButton(
                    selected = selectedOption == option,
                    onClick = { onOptionSelected(option) }
                )
            }
        }
    }
}

@Composable
fun PaymentMethodsSection(
    selectedMethod: String,
    onMethodSelected: (String) -> Unit
) {
    Column {
        listOf("Credit/Debit Card", "Net Banking", "UPI", "Cash on Delivery").forEach { method ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = method, fontSize = 16.sp)
                RadioButton(
                    selected = selectedMethod == method,
                    onClick = { onMethodSelected(method) }
                )
            }
        }
    }
}

@Composable
fun OrderSummarySection(
    total: Int
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.large // Larger rounded corners
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Row for Title and Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Space between title and amount
                verticalAlignment = Alignment.CenterVertically // Center align vertically
            ) {
                // Title
                Text(
                    text = "To Pay",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif, // Modern sans-serif font
                )

                // Total amount
                Text(
                    text = "Rs. $total",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(252, 95, 82),
                    fontFamily = FontFamily.Serif,
                )
            }
        }
    }
}
