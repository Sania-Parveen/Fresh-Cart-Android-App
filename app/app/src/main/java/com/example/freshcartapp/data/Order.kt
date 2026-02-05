package com.example.freshcartapp.data

data class Order(
    val userName: String,
    val id: String,
    val contact: String,
    val address: String,
    val email: String,
    val items: List<InternetItem>
)
