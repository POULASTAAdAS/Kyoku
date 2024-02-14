package com.poulastaa.kyoku.presentation.screen.setup.birth_date

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.BDateFroMaterHelperStatus
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.auth.UiEvent
import com.poulastaa.kyoku.data.model.setup.set_birth_date.SetBirthDateUiEvent
import com.poulastaa.kyoku.data.model.setup.set_birth_date.SetBirthDateUiState
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.utils.storeBDate
import com.poulastaa.kyoku.utils.storeSignInState
import com.poulastaa.kyoku.utils.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetBirthDateViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val ds: DataStoreOperation
) : ViewModel() {
    private val network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)

    init {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
                state = state.copy(
                    isInternetAvailable = checkInternetConnection()
                )
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        return network.value == NetworkObserver.STATUS.AVAILABLE
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(SetBirthDateUiState())
        private set

    fun onEvent(event: SetBirthDateUiEvent) {
        when (event) {
            is SetBirthDateUiEvent.OnDateSelected -> {
                state = if (event.date.length > 3) {
                    val date = event.date.toLong().toDate()

                    Log.d("date", date.toString())

                    when (date.status) {
                        BDateFroMaterHelperStatus.OK -> {
                            state.copy(
                                bDate = date.date,
                                isDialogOpen = false,
                                isError = false
                            )
                        }

                        BDateFroMaterHelperStatus.TO_OLD -> {
                            onEvent(SetBirthDateUiEvent.EmitToast("You can't be that Old"))

                            state.copy(
                                bDate = date.date,
                                isDialogOpen = false
                            )
                        }

                        BDateFroMaterHelperStatus.TO_YOUNG -> {
                            onEvent(SetBirthDateUiEvent.EmitToast("To Young to use application"))
                            state.copy(
                                bDate = date.date,
                                isDialogOpen = false,
                            )
                        }

                        BDateFroMaterHelperStatus.FROM_FUTURE -> {
                            onEvent(SetBirthDateUiEvent.EmitToast("Holy Cow you from Future ?"))
                            state.copy(
                                bDate = date.date,
                                isDialogOpen = false
                            )
                        }
                    }
                } else {
                    state.copy(
                        bDate = event.date,
                        isDialogOpen = false
                    )
                }
            }

            SetBirthDateUiEvent.OnDateSelectorClicked -> {
                state = state.copy(
                    isDialogOpen = true
                )
            }

            SetBirthDateUiEvent.OnContinueClick -> {
                if (state.bDate.isEmpty() || state.isError) {
                    onEvent(SetBirthDateUiEvent.EmitToast("Please Select Your Birth Date"))
                    return
                }

                // todo make api call 

                storeBDate(state.bDate, ds)
                storeSignInState(SignInStatus.GENRE_SET, ds)
            }

            is SetBirthDateUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }
        }
    }
}