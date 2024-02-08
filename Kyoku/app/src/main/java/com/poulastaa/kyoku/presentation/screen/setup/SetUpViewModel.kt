package com.poulastaa.kyoku.presentation.screen.setup

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
class SetUpViewModel @Inject constructor(
    private val ds: DataStoreOperation
) : ViewModel() {
    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            ds.storeSignedInState(SignInStatus.AUTH.name)
        }
    }

    fun showData() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(
                "data",
                "username: ${ds.readUsername().collect()} \nprofilePic url: ${
                    ds.readProfilePic().collect()
                }\ncookie: ${ds.readTokenOrCookie().collect()}"
            )
        }
    }
}