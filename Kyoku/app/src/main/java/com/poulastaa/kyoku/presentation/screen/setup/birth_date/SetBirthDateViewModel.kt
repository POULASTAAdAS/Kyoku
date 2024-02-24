package com.poulastaa.kyoku.presentation.screen.setup.birth_date

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateReq
import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateResponseStatus
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.setup.set_b_date.SetBirthDateUiEvent
import com.poulastaa.kyoku.data.model.screens.setup.set_b_date.SetBirthDateUiState
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.utils.BDateFroMaterHelperStatus
import com.poulastaa.kyoku.utils.storeBDate
import com.poulastaa.kyoku.utils.storeSignInState
import com.poulastaa.kyoku.utils.toDate
import com.poulastaa.kyoku.utils.toSetBDateReq
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetBirthDateViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val ds: DataStoreOperation,
    private val service: ServiceRepository
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

    private var bDateAsLong: Long? = null

    fun onEvent(event: SetBirthDateUiEvent) {
        when (event) {
            is SetBirthDateUiEvent.OnDateSelected -> {
                state = if (event.date.length > 3) {
                    val date = event.date.toLong().toDate()

                    bDateAsLong = event.date.toLong()

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
                if (state.bDate.isEmpty() || state.isError || bDateAsLong == null) {
                    onEvent(SetBirthDateUiEvent.EmitToast("Please Select Your Birth Date"))
                    return
                }

                state = state.copy(
                    isLoading = true
                )
                sendBDateToServer()
            }

            is SetBirthDateUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }
        }
    }

    private fun sendBDateToServer() {
        viewModelScope.launch(Dispatchers.IO) {
            val email = ds.readEmail().first()

            makeApiCall(bDateAsLong!!.toSetBDateReq(email))
        }
    }

    private fun makeApiCall(req: SetBDateReq) {
        viewModelScope.launch(Dispatchers.IO) {
            service.sendBDateToServer(req).let {
                when (it.status) {
                    SetBDateResponseStatus.SUCCESS -> {
                        storeBDate(state.bDate, ds)
                        storeSignInState(SignInStatus.GENRE_SET, ds)
                    }

                    SetBDateResponseStatus.FAILURE -> {
                        onEvent(SetBirthDateUiEvent.EmitToast("Opp's Something went wrong"))
                    }
                }
                state = state.copy(
                    isLoading = false
                )
            }
        }
    }
}