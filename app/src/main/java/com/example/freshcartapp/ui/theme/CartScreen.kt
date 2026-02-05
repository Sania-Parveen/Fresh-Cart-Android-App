package com.example.freshcartapp.ui.theme


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.freshcartapp.R
import com.example.freshcartapp.data.InternetItem
import com.example.freshcartapp.data.InternetItemWithQuantity

@Composable
fun CartScreen(
    freshViewModel: FreshViewModel,
    onHomeButtonClicked: () -> Unit,
    onCheckoutButtonClicked: () -> Unit

) {

    val cartItems by freshViewModel.cartItems.collectAsState()
    val cartItemWithQuantity = cartItems.groupBy { it }
        .map {
                (item, cartItems) -> InternetItemWithQuantity(
            item,
            cartItems.size)
        }

    if(cartItems.isNotEmpty()){
        LazyColumn(
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.sale_topbar),
                    contentDescription = "Offer",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(135.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                Text(
                    text = "Review Items",
                    //color = Color(252, 95, 82, 255),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            items(cartItemWithQuantity) {
                CartCard(
                    it.item,
                    freshViewModel,
                    it.quantity,
                    it.item.itemName,
                    it.item.imageUrl,
                    it.item.itemQuantity,
                    it.item.itemPrice
                )
            }

            item {
                Text(
                    text = "Bill Details",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            val totalPrice =  cartItems.sumOf { it.itemPrice * 75/100 }
            val handlingCharge = totalPrice*1/100
            val deliveryFee = 10
            val gst = totalPrice*2/100
            val grandTotal = totalPrice + handlingCharge + deliveryFee + gst

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Unspecified
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(18.dp)
                    ) {
                        BillRow(itemName = "Item Total", itemPrice = totalPrice, fontWeight = FontWeight.Normal )
                        BillRow(itemName = "Handling Charge", itemPrice = handlingCharge, fontWeight = FontWeight.Light )
                        BillRow(itemName = "Delivery Fee", itemPrice = deliveryFee, fontWeight = FontWeight.Light )
                        BillRow(itemName = "Gst", itemPrice = gst, fontWeight = FontWeight.Light )
                        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 5.dp), color = Color.LightGray)
                        BillRow(itemName = "Total", itemPrice = grandTotal, fontWeight = FontWeight.ExtraBold )
                    }
                }
            }

            // Checkout Button
            item {
                Button(
                    onClick = {
                        onCheckoutButtonClicked()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(252, 95, 82, 255)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Checkout")
                }
            }

        }

    } else{
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.emptycart), contentDescription = "App Icon",
                modifier = Modifier
                    .size(300.dp)
            )

            Text(
                text = "Your Cart is Empty",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp)
            )

            FilledTonalButton(onClick = {
                onHomeButtonClicked()
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(252, 95, 82, 255))
            ) {
                Text(text = "Shop Now")

            }
        }

    }

}


@Composable
fun CartCard(
    cartItem: InternetItem,
    freshViewModel: FreshViewModel,
    cartItemQuantity: Int,
    stringResourceId: String,
    imageResourceId: String,
    itemQuantity: String,
    itemPrice: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(1.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = cartItem.imageUrl,
            contentDescription = "Item image",
            modifier = Modifier
                .size(60.dp)
                .padding(start = 2.dp)
        )

        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(4f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = cartItem.itemName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1
            )
            Text(
                text = cartItem.itemQuantity,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .weight(3f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Rs. ${cartItem.itemPrice}",
                fontSize = 12.sp,
                color = Color.Gray,
                textDecoration = TextDecoration.LineThrough
            )
            Text(
                text = "Rs. ${cartItem.itemPrice * 75 / 100}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .weight(3f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Qty: $cartItemQuantity",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        freshViewModel.removeFromCart(oldItem = cartItem)
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(123, 187, 180)
                    ),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(9.dp),
//                    shape = RectangleShape
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.
                        minus_solid), // Assuming your minus icon is named minus_solid.xml
                        contentDescription = "Remove",
                        tint = Color.White,
                        modifier = Modifier.padding(4.dp)
                    )
                }

                IconButton(
                    onClick = {
                        freshViewModel.addToDatabase(
                            InternetItem(
                                itemName = stringResourceId,
                                itemQuantity = itemQuantity,
                                itemPrice = itemPrice,
                                imageUrl = imageResourceId,
                                itemCategory = ""
                            )
                        )
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(123, 187, 180 )
                    ),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(9.dp),
//                    shape = RectangleShape
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.plus_solid), // Assuming your minus icon is named minus_solid.xml
                        contentDescription = "ADD",
                        tint = Color.White,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }

    }
}
@Composable
fun BillRow(
    itemName: String,
    itemPrice: Int,
    fontWeight: FontWeight

){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = itemName, fontWeight = fontWeight)
        Text(text = "Rs. $itemPrice", fontWeight = fontWeight)
    }

}
