package com.poulastaa.setup.presentation.set_b_date

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.domain.repository.b_date.BDateRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class BDateViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repository: BDateRepository,
) : ViewModel() {
    var state by mutableStateOf(BDateUiState())
        private set

    private val _uiEvent = Channel<BDateUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var storeBDateJob: Job? = null

    fun onEvent(event: BDateUiEvent) {
        when (event) {
            BDateUiEvent.OnBackClick -> {
                storeBDateJob?.cancel()
                viewModelScope.launch {
                    _uiEvent.send(BDateUiAction.NavigateToSpotifyPlaylist)
                }
            }

            BDateUiEvent.OnBDateDialogToggle -> {
                state = state.copy(
                    isDateDialogOpen = !state.isDateDialogOpen
                )
            }

            is BDateUiEvent.OnBDateSubmit -> {
                val longBDate = event.value ?: return

                state = state.copy(
                    bDate = state.bDate.copy(
                        data = longBDate.toDateString(),
                        isErr = false,
                        errText = UiText.DynamicString("")
                    ),
                    canMakeReq = true
                )
            }

            BDateUiEvent.OnContinueClick -> {
                if (isValid()) return

                state = state.copy(
                    isMakingApiCall = true
                )

                storeBDateJob?.cancel()
                storeBDateJob = storeBDate()
            }
        }
    }

    private fun isValid(): Boolean {
        if (state.bDate.data.isEmpty()) {
            state = state.copy(
                bDate = state.bDate.copy(
                    isErr = true,
                    errText = UiText.StringResource(R.string.error_empyt_BDate)
                )
            )

            return true
        }

        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val bDate = LocalDate.parse(state.bDate.data, dateTimeFormatter)

        val currentDate = LocalDate.now()
        val age = ChronoUnit.YEARS.between(bDate, currentDate)

        if (age < 8) {
            state = state.copy(
                bDate = state.bDate.copy(
                    isErr = true,
                    errText = UiText.StringResource(R.string.error_bDate_too_young)
                )
            )

            return true
        }

        if (age > 99) {
            state = state.copy(
                bDate = state.bDate.copy(
                    isErr = true,
                    errText = UiText.StringResource(R.string.error_bDate_too_old)
                )
            )

            return true
        }

        return false
    }

    private fun storeBDate() = viewModelScope.launch(Dispatchers.IO) {
        val date = LocalDate.parse(state.bDate.data, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        val longBDate = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        when (val response = repository.storeBDate(longBDate)) {
            is Result.Error -> {
                when (response.error) {
                    DataError.Network.NO_INTERNET -> {
                        _uiEvent.send(
                            BDateUiAction.EmitToast(UiText.StringResource(R.string.error_no_internet))
                        )
                    }

                    else -> {
                        _uiEvent.send(
                            BDateUiAction.EmitToast(UiText.StringResource(R.string.error_something_went_wrong))
                        )
                    }
                }
            }

            is Result.Success -> {
                ds.storeSignInState(ScreenEnum.PIC_GENRE)

                _uiEvent.send(
                    BDateUiAction.EmitToast(UiText.StringResource(R.string.success_bDate_stored))
                )

                _uiEvent.send(
                    BDateUiAction.NavigateToSetGenre
                )
            }
        }

        state = state.copy(
            isMakingApiCall = false,
        )

        storeBDateJob?.cancel()
    }
}