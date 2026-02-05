package com.example.freshcartapp.ui.theme


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshcartapp.R
import com.example.freshcartapp.data.DataSource

@Composable
fun StartScreen(
    freshViewModel: FreshViewModel,
    onCategoryClicked:(Int)-> Unit
){
    val context = LocalContext.current

    LazyVerticalGrid(columns = GridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)

    ){

        item(
            span = { GridItemSpan(2) }
        ) {
            Column(  modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
            ) {
                Image(painter = painterResource(id = R.drawable.delivery_topbar),
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
                        text = "Shop by Category",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .padding(1.dp)
                            .align(Alignment.CenterHorizontally))

                }
            }

        }

        items(DataSource.loadCategories()){
            CategoryCard(context = context,
                stringResourceId = it.stringResourceId,
                imageResourceId = it.imageResourceId,
                freshViewModel = freshViewModel,
                onCategoryClicked = onCategoryClicked
            )
        }

    }
}


@Composable
fun CategoryCard(
    context: Context,
    stringResourceId: Int,
    imageResourceId: Int,
    freshViewModel: FreshViewModel,
    onCategoryClicked: (Int) -> Unit
) {
    val categoryName = stringResource(id = stringResourceId)
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                freshViewModel.updateClickText(categoryName)
                Toast.makeText(context, categoryName, Toast.LENGTH_SHORT).show()
                onCategoryClicked(stringResourceId)
            }
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = categoryName,
                color = Color.Black,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )

            Image(
                painter = painterResource(id = imageResourceId),
                contentDescription = categoryName,
                modifier = Modifier
                    .size(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
            )

        }
    }
}
