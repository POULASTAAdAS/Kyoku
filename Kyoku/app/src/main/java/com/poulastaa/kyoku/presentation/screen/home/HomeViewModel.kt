package com.poulastaa.kyoku.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    }
}