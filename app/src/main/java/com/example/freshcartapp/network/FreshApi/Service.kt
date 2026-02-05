package com.example.freshcartapp.network.FreshApi

import com.example.freshcartapp.data.InternetItem
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://item-gz3d.onrender.com"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(
        Json.asConverterFactory("application/json".toMediaType())
    )
    .baseUrl(BASE_URL)
    .build()

/********************** CREATING AN INTERFACE FOR RETROFIT ******************/
/*** API stands for Application Programming Interface. It is a set of rules and protocols that allows different software applications to communicate with each other. APIs define the methods and data formats that applications can use to request and exchange information. */

interface FreshApiService{
    @GET("/items")
    suspend fun getItems(): List<InternetItem>    /** when we call a function from a viewModel() scope we use suspend keyword before fun keyword to ensure that these function can be handled by coroutines. */
    /** a function with the suspend keyword does not block the thread it is running on. This means that even if we suspend a function performing a network operation, the other part of the UI will still remain functional and the network request will be carried out in the background. */
}

/********************** RETROFIT SERVICE ******************/
/** Lazy Initialization with Retrofit
This means delaying the creation of the service instance until it's actually needed.
Benefits of Lazy Initialization:
Improved Startup Time: If the service isn't immediately required, delaying its creation can speed up the app's initial launch.
Resource Efficiency: Resources like RAM and CPU are only consumed when the service is actively used. */

object FreshApi{
    val retrofitService: FreshApiService by lazy {
        retrofit.create(FreshApiService::class.java)
    }

}