package com.example.freshcartapp.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.freshcartapp.R
import com.example.freshcartapp.data.InternetItem
import com.google.firebase.auth.FirebaseAuth


enum class FreshCartAppScreen(
    val title: String
){
    Start("FreshCart"),
    Items("Choose Items"),
    Cart("Your Cart"),
    Checkout("Checkout"),
    OrderConfirmation("Order Now")
}

//create a boolean variable canNavigateBack which tells whether to show a back icon/button on the topAppBar or Not. Because Back button should only be visible if there is any other screen in the back-stack. Back button should not be visible on the first default screen.
var canNavigateBack = false
val auth = FirebaseAuth.getInstance()  // Initialize Firebase Auth instance and stored in a variable "auth". Now we can use this variable to interact with Firebase Authentication.

@OptIn(ExperimentalMaterial3Api::class)


@Composable
fun FreshCartApp(freshViewModel: FreshViewModel = viewModel(),
                 navController : NavHostController = rememberNavController()
) {
    val user by freshViewModel.user.collectAsState()
    val logoutClicked by freshViewModel.logoutClicked.collectAsState()
    //auth.currentUser?.let {freshViewModel.setUser(it)}
    freshViewModel.setUser(auth.currentUser)

    val isVisible by freshViewModel.isVisible.collectAsState()

    //giving back-stack reference
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen =
        FreshCartAppScreen.valueOf(       //currentScreen is the variable that store the current status/screen of the app
            backStackEntry?.destination?.route
                ?: FreshCartAppScreen.Start.name   //  "?:" -> is called as Elvis Operator. it is used to check whether the called route by the vavController in the app can be matched to defined screen in the enum class.
        )


//below line ensures that canNavigateBack assigns with true if Back-Stack is not equals to Null, else remain false
    canNavigateBack = navController.previousBackStackEntry != null

    val cartItems by freshViewModel.cartItems.collectAsState()

    if(isVisible){
        OfferScreen()
    } else if(user == null){
        LoginUi(freshViewModel = freshViewModel)
    }

    else{
        /**************** Scaffold is used to create bars in app like topAppBar and bottomAppBar , floatingActionBar *************/
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()

                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {

                                Text(
                                    text = currentScreen.title,
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    color = Color.Black
                                )

                                if (currentScreen == FreshCartAppScreen.Cart) {
                                    Text(
                                        text = "(${cartItems.size})",
                                        fontSize = 20.sp,
                                        fontFamily = FontFamily.SansSerif,
                                        color = Color.Black
                                    )

                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.clickable {
                                    freshViewModel.setLogoutStatus(true)
                                }
                            ) {
                                Icon(painter = painterResource(id = R.drawable.logout), contentDescription ="Logout",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding()
                                )

                                Text(text ="Logout",
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    modifier = Modifier.padding(end = 14.dp, start = 4.dp)
                                )

                            }
                        }
                    },
                    //navigationIcon{} is used to display the icon on the TopAppBar
                    navigationIcon = {
                        if (canNavigateBack) {
                            IconButton(onClick = {
                                navController.navigateUp()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back Button"
                                )


                            }
                        }

                    }
                )
            },

            bottomBar = {
                FreshCartAppBar(
                    navController = navController,
                    currentScreen = currentScreen,
                    cartItems = cartItems
                )
            },
        ) {

            NavHost(
                navController = navController,
                startDestination = FreshCartAppScreen.Start.name,
                Modifier.padding(it)
            ) {
                composable(route = FreshCartAppScreen.Start.name) {
                    StartScreen(
                        freshViewModel = freshViewModel,
                        onCategoryClicked = {
                            freshViewModel.updateSelectedCategory(it)
                            navController.navigate(FreshCartAppScreen.Items.name)
                        }
                    )
                }

                composable(route = FreshCartAppScreen.Items.name) {
                    InternetItemsScreen(freshViewModel = freshViewModel,
                        itemUiState = freshViewModel.itemUiState)
                }

                composable(route = FreshCartAppScreen.Cart.name) {
                    CartScreen(
                        freshViewModel = freshViewModel,
                        onHomeButtonClicked = {
                            navController.navigate(FreshCartAppScreen.Start.name) {
                                popUpTo(0)
                            }
                        },
                        onCheckoutButtonClicked = { // Add this line
                            navController.navigate(FreshCartAppScreen.Checkout.name) // Navigate to Checkout screen
                        }
                    )
                }

                composable(route = FreshCartAppScreen.Checkout.name) {
                    CheckoutPage(
                        freshViewModel = freshViewModel,

                        onOrderConfirmed ={
                            navController.navigate(FreshCartAppScreen.OrderConfirmation.name)

                        }
                    )
                }

                composable(route = FreshCartAppScreen.OrderConfirmation.name) {
                    CongratulationOrderSuccessful(
                        onHomeButtonClicked = {
                            navController.navigate(FreshCartAppScreen.Start.name) {
                                popUpTo(0)
                            }
                        },
                        onContinueShopping = {
                            navController.navigate(FreshCartAppScreen.Start.name) {
                                popUpTo(0)
                            }
                        }
                    )
                }






            }

        }
        if(logoutClicked){
            AlertCheck(onYesButtonPressed = {
                freshViewModel.setLogoutStatus(false)
                auth.signOut()
                freshViewModel.clearData()
            },
                onNoButtonPressed =  {
                    freshViewModel.setLogoutStatus(false)

                }
            )
        }



    }
}
/******************* Fresh App Button Bar Like Home And Cart...Create a composable function to make the Bar and Call It inside bottomAppBar{freshCartAppBar()} *************/

@Composable
fun FreshCartAppBar(
    navController: NavHostController,
    currentScreen: FreshCartAppScreen,
    cartItems : List<InternetItem>
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 70.dp,
                vertical = 10.dp
            )
    ) {

        /************ Home Icon ***********/

        /************ Home Icon ***********/

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                navController.navigate(FreshCartAppScreen.Start.name){
                    popUpTo(0 )
                }

            }
        ) {
            Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home")

            Text(text = "Home", fontSize = 10.sp)
        }

        /************ CART Icon ***********/

        /************ CART Icon ***********/

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                if(currentScreen!= FreshCartAppScreen.Cart) {
                    navController.navigate(FreshCartAppScreen.Cart.name) {
                    }
                }
            }
        ) {
            Box {
                Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = "Cart")

                if(cartItems.isNotEmpty() )
                    Card(
                        modifier = Modifier.align(
                            alignment = Alignment.TopEnd
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text(
                            text = cartItems.size.toString(),
                            fontSize = 10.sp,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .padding(horizontal = 1.dp)

                        )

                    }
            }

            Text(text = "Cart", fontSize = 10.sp)
        }

    }
}

@Composable
fun AlertCheck(
    onYesButtonPressed: () -> Unit,
    onNoButtonPressed: () -> Unit
){

    AlertDialog(
        containerColor = Color.White,
        title = {
            Text(text = "Logout?", fontWeight = FontWeight.Bold)
        },
        text = {
            Text(text = "Are you sure you want to log out?")
        },
        confirmButton = {
            TextButton(onClick = {
                onYesButtonPressed()
            }) {
                Text(text = "Yes")

            }
        },

        dismissButton = {
            TextButton(onClick = {
                onNoButtonPressed()
            }) {
                Text(text = "No")
            }
        },
        onDismissRequest = {
            onNoButtonPressed()
        }

    )

}
