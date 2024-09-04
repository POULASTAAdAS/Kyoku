package com.poulastaa.play.presentation.root_drawer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.poulastaa.core.domain.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootDrawerViewModel_1 @Inject constructor(
    private val ds: DataStoreRepository
) : ViewModel() {
    var state by mutableStateOf(RootDrawerUiState())
        private set
}