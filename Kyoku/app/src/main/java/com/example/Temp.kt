package com.example

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.kyoku.BuildConfig
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun Test() {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.CLIENT_ID)
        .requestEmail()
        .build()


    val account = gso.account

    Log.d("account" , account.toString())

    account?.let {
        Log.d("name", it.name)
        Log.d("name", it.type)
    }
}