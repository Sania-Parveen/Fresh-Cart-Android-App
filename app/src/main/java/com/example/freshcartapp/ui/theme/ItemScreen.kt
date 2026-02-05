package com.example.freshcartapp.ui.theme

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.freshcartapp.R
import com.example.freshcartapp.data.InternetItem

@Composable
fun ItemsScreen(
    freshViewModel: FreshViewModel,
    items: List<InternetItem>
) {
    val freshUiState by freshViewModel.uiState.collectAsState()
    val selectedCategory = stringResource(id = freshUiState.selectedCategory)

    val database = items.filter {
        it.itemCategory.lowercase() == selectedCategory.lowercase()
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item(
            span = { GridItemSpan(2) }
        ) {
            Column( modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
            ) {
                Image(painter = painterResource(id = R.drawable.sale_topbar),
                    contentDescription = "offer",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(135.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop

                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(123, 187, 180)
                    ),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                ){
                    Text(

                        text = "${stringResource(id = freshUiState.selectedCategory)} (${database.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .padding(1.dp)
                            .align(Alignment.CenterHorizontally))

                }
            }

        }

        items(database) {
            ItemCard(
                stringResourceId = it.itemName,
                imageResourceId = it.imageUrl,
                itemQuantity = it.itemQuantity,
                itemPrice = it.itemPrice,
                freshViewModel = freshViewModel
            )

        }
    }
}

@Composable
fun InternetItemsScreen(
    freshViewModel: FreshViewModel,
    itemUiState: FreshViewModel.ItemUiState
) {
    when (itemUiState) {
        is FreshViewModel.ItemUiState.Loading -> { LoadingScreen() }
        is FreshViewModel.ItemUiState.Success -> {
            ItemsScreen(freshViewModel = freshViewModel, items = itemUiState.items)
        }
        else -> { ErrorScreen(freshViewModel = freshViewModel) }

    }
}

@Composable
fun ItemCard(
    stringResourceId: String,
    imageResourceId: String,
    itemQuantity: String,
    itemPrice: Int,
    freshViewModel: FreshViewModel
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                AsyncImage(
                    model = imageResourceId,
                    contentDescription = stringResourceId,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Top
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(252, 95, 82)
                        )
                    ) {
                        Text(
                            text = "25% OFF",
                            color = Color.White,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(
                                horizontal = 4.dp,
                            )
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResourceId,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Rs. $itemPrice",
                        fontSize = 10.sp,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        color = Color(109,109,109,255),
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(text = "Rs. ${itemPrice*75/100}",
                        fontSize = 15.sp,
                        maxLines = 1 ,
                        textAlign = TextAlign.Center,
                        color = Color(252, 95, 82, 255)
                    )
                }

                Text(
                    text = itemQuantity,
                    fontSize = 14.sp,
                    maxLines = 1,
                    color = Color(114,114,114,255)
                )

            }

            Button(
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
                    Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(123, 187, 180)//0, 150, 136
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 7.dp)

            ) {
                Text(
                    text = "Add to Cart",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(){
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(painter = painterResource(id = R.drawable.loading), contentDescription = "Loading")
    }
}


@Composable
fun ErrorScreen(freshViewModel: FreshViewModel){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(painter = painterResource(id = R.drawable.internet_unavaiable), contentDescription = "Error",
            modifier = Modifier.size(250.dp))
        Text(
            text = "OOPS! Internet not available Please RETRY after turning on the Internet connection",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        Button(onClick = {
            freshViewModel.getFreshItems()
        }
        ) {
            Text(text = "Retry")
        }
    }
}


