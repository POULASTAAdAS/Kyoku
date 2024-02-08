package com.poulastaa.kyoku.presentation.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ds: DataStoreOperation
) : ViewModel() {
    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            ds.storeSignedInState(SignInStatus.AUTH.name)
        }
    }

    fun showData() {
        viewModelScope.launch(Dispatchers.IO) {
            ds.readUsername().collect {
                Log.d("data", "username: $it")
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            ds.readProfilePic().collect {
                Log.d("data", "profilePic: $it")
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            ds.readTokenOrCookie().collect {
                Log.d("data", "cookie: $it")
            }
        }
    }
}