package com.example.freshcartapp.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshcartapp.data.InternetItem
import com.example.freshcartapp.data.Order
import com.example.freshcartapp.network.FreshApi
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class FreshViewModel: ViewModel(){

    /**
     * _uiState is a private MutableStateFlow that holds the current UI state.
     * uiState is a public immutable StateFlow that exposes the UI state to other classes.
     */

    private val _uiState = MutableStateFlow(FreshUiState())
    val uiState: StateFlow<FreshUiState> = _uiState.asStateFlow()

    /**
     * _isVisible is a private MutableStateFlow that controls the visibility of a UI element.
     * isVisible is a public StateFlow that exposes the visibility state to other classes.
     */

    private val _isVisible = MutableStateFlow(true)
    val isVisible = _isVisible

    /** The mutableStateOf() function isa part of jetpack Compose. It represents a state that can be observed,and any changes to the state will trigger recomposition of the UI*/
    var itemUiState: ItemUiState by mutableStateOf(ItemUiState.Loading)
        private set

    /*********************** _user holds firebase user ***********************/
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: MutableStateFlow<FirebaseUser?> get() = _user

    /*********************** _phoneNumber holds phone number ***********************/
    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber : MutableStateFlow<String> get() = _phoneNumber


    /*********************** _cartItem holds list of internet items ***********************/
    private val _cartItems = MutableStateFlow<List<InternetItem>>(emptyList())
    val cartItems: StateFlow<List<InternetItem>> get()= _cartItems.asStateFlow()

    /*********************** _otp holds otp ***********************/
    private val _otp = MutableStateFlow("")
    val otp : MutableStateFlow<String> get() = _otp


    /*********************** _verificationId holds verificationId ***********************/
    private val _verificationId = MutableStateFlow("")
    val verificationId : MutableStateFlow<String> get() = _verificationId

    /*********************** _ticks holds ticks ***********************/
    private val _ticks = MutableStateFlow(60L)
    val ticks : MutableStateFlow<Long> get() = _ticks

    private lateinit var timerJob: Job

    private val database = Firebase.database
    private val myRef = database.getReference("USERS/${auth.currentUser?.uid}/CART")

    private val _loading = MutableStateFlow(false)
    val loading : MutableStateFlow<Boolean> get() = _loading


    private val _logoutClicked = MutableStateFlow(false)
    val logoutClicked : MutableStateFlow<Boolean> get() = _logoutClicked

    // Additional MutableStateFlow to handle orders
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> get() = _orders

    private val _currentOrder = MutableStateFlow<Order?>(null)
    val currentOrder: StateFlow<Order?> get() = _currentOrder


    /*********************** JOB ***********************/
    private lateinit var internetJob: Job          /** Represents the coroutine job for internet-related tasks.*/
    private var offerscreenJob: Job       /** Represents the coroutine job for tasks related to displaying the offer screen. */

    /*********************** ITEM UI STATE ***********************/

    sealed interface ItemUiState {
        data class Success(val items: List<InternetItem>) : ItemUiState
        data object Error : ItemUiState
        data object Loading : ItemUiState
    }

    fun setVerificationId(verificationId: String){
        _verificationId.value = verificationId
    }

    fun setUser(user: FirebaseUser?){
        _user.value = user
    }

    fun clearData(){
        _user.value = null
        _phoneNumber.value = ""
        _otp.value = ""
        _verificationId.value = ""
        resetTimer()
    }

    fun setOtp(otp: String){
        _otp.value = otp

    }

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun runTimer(){
        timerJob = viewModelScope.launch {
            while(_ticks.value > 0){
                delay(1000)
                _ticks.value -= 1

            }

        }
    }



    fun placeOrder(
        username: String,
        contactNumber:String,
        address: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val newOrder = Order(
            userName = username,
            id = UUID.randomUUID().toString(),
            address = address,
            email = email,
            contact = contactNumber,
            items = _cartItems.value
        )

        // Store order in the database

        database.getReference("USERS/${auth.currentUser?.uid}/ORDER").push().setValue(newOrder)
            .addOnSuccessListener {
                _cartItems.value = emptyList()
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }


    fun resetTimer(){
        try {
            timerJob.cancel()
        }catch (_: Exception){

        }finally {
            _ticks.value = 60L
        }


    }

    fun setLoading(isLoading: Boolean){
        _loading.value = isLoading

    }

    fun setLogoutStatus(logoutStatus: Boolean){
        _logoutClicked.value = logoutStatus
    }


    fun addToCart(item: InternetItem) {
        _cartItems.value += item
    }

    fun addToDatabase(item: InternetItem) {
        myRef.push().setValue(item)
    }

    // Function to read cart items from the database
    private fun fillCartItems(){
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _cartItems.value = emptyList()
                for (childSnapshot in dataSnapshot.children){
                    val item = childSnapshot.getValue(InternetItem::class.java)
                    item?.let {
                        val newItems = it
                        addToCart(newItems)
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    fun removeFromCart(oldItem: InternetItem) {
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children){
                    var itemRemoved = false
                    val item = childSnapshot.getValue(InternetItem::class.java)
                    item?.let {
                        if(oldItem.itemName == it.itemName && oldItem.itemPrice == it.itemPrice){
                            childSnapshot.ref.removeValue()
                            itemRemoved = true
                        }

                    }
                    if(itemRemoved) break
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        )
    }

    /**
     * Updates the click status text in the UI state.
     *  The new text to update the click status with.
     */

    fun updateClickText(updatedText: String){
        _uiState.update {
            it.copy(
                clickStatus = updatedText
            )
        }
    }

    fun updateSelectedCategory(updatedCategory: Int){
        _uiState.update {
            it.copy(
                selectedCategory = updatedCategory
            )
        }

    }

    /*********************** TOGGLE VISIBILITY ***********************/
    /**
     * Toggles the visibility state of the offer screen UI element to false.
     * This hides the offer screen UI element.
     */
    /**
     * Sets the value of _isVisible to false, delaying the display of the offer screen for 2 seconds.
     * This delay ensures that the offer screen is shown briefly before transitioning to the Category screen.
     */

    private fun toggleVisibility() {
        _isVisible.value = false
    }

    /** we've already created a similar function in FreshApiService.kt file as "fun getItems(): String" which fetches the data from the internet. Then what is the need of creating another function "fun getFreshItems()" ?.
     *  It's bcz getItem() function is only responsible for fetching the data from the internet but cannot pass the fetched data to our app. Therefore we created another function getFreshItems() which will act as the bridge betwwen  the getItem() and our app UI so that whenever the getItem() function fetches data from the internet, the same data can be passed to our app UI.*/

    fun getFreshItems() {
        internetJob = viewModelScope.launch {
            try {
                val listResult = FreshApi.retrofitService.getItems() //fetched data is stored in the listResult variable so that we can use it
                itemUiState = ItemUiState.Success(listResult)
            }
            catch (exception: Exception){
                itemUiState = ItemUiState.Error
                toggleVisibility()
                offerscreenJob.cancel()
            }
        }
    }

    /*********************** INIT BLOCK ***********************/
    /** The init block is executed when the ViewModel is created. It launches a coroutine in the viewModelScope, which delays for 3000 milliseconds (3 seconds) before calling toggleVisibility() to update the visibility state */

    init{
        offerscreenJob = viewModelScope.launch(Dispatchers.Default) {             /**viewModelScope is an extension property available in ViewModel. It is tied to the lifecycle of the ViewModel. When the ViewModel is cleared (i.e., when it is no longer needed and is being destroyed), any coroutines launched in viewModelScope are automatically cancelled.*/
            delay(2000)              /** When you use viewModelScope.launch {}, you are launching a coroutine within the viewModelScope. This ensures that the coroutine is automatically cancelled when the ViewModel is cleared, preventing memory leaks and ensuring proper lifecycle management. */
            toggleVisibility()
        }
        getFreshItems()
        fillCartItems()
    }

}