package com.example.freshcartapp.ui.theme

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.freshcartapp.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

@Composable
fun LoginUi(freshViewModel: FreshViewModel){

    val context = LocalContext.current
    val otp by freshViewModel.otp.collectAsState()
    val verificationId by freshViewModel.verificationId.collectAsState()
    val loading by freshViewModel.loading.collectAsState()

    // Initialize PhoneAuthProvider.OnVerificationStateChangedCallbacks
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.


        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.


            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            freshViewModel.setVerificationId(verificationId)
            Toast.makeText(context, "OTP Sent", Toast.LENGTH_SHORT).show()
            freshViewModel.resetTimer()
            freshViewModel.runTimer()
            freshViewModel.setLoading(false)

        }
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.app_iconn),
                contentDescription = "App Icon",
                modifier = Modifier
                    .padding(
                        top = 50.dp,
                        bottom = 10.dp
                    )
                    .size(120.dp)
            )

            if (verificationId.isEmpty()) {
                NumberScreen(freshViewModel = freshViewModel, callbacks = callbacks)

            } else {
                OtpScreen(otp = otp, freshViewModel = freshViewModel, callbacks = callbacks)
            }

        }

        if(verificationId.isNotEmpty()){
            IconButton(onClick = {
                freshViewModel.setVerificationId("")
                freshViewModel.setOtp("")

            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back Button")

            }

        }

        if(loading){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(255, 255, 255, 190))
            ) {
                CircularProgressIndicator()
                Text(text = "Loading...")
            }
        }

    }

}