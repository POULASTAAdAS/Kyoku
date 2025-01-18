package com.poulastaa.setup.presentation.set_bdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.setup.domain.repository.set_bdate.BDateRepository
import com.poulastaa.setup.domain.repository.set_bdate.BDateValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetBDateViewmodel @Inject constructor(
    private val validator: BDateValidator,
    private val repo: BDateRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SetBDateUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SetBDateUiState()
    )

    private val _uiEvent = Channel<SetBDateUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: SetBDateUiAction) {
        when (action) {
            SetBDateUiAction.OnBackClick -> {
                if (_state.value.isMakingApiCall) return

                viewModelScope.launch {
                    _uiEvent.send(SetBDateUiEvent.NavigateBack)
                }
            }

            SetBDateUiAction.OnDateDialogToggle -> {
                if (_state.value.isMakingApiCall) return

                _state.update {
                    it.copy(
                        isDialogOpn = !it.isDialogOpn
                    )
                }
            }

            is SetBDateUiAction.OnDateChange -> {
                val type = validator.validate(action.date)

                _state.update {
                    it.copy(
                        date = it.date.copy(
                            value = validator.toDate(action.date),
                            isErr = type.isErr,
                            errText = UiText.DynamicString(type.message)
                        ),
                        isDialogOpn = false
                    )
                }
            }

            SetBDateUiAction.OnSubmitClick -> {
                if (_state.value.date.value.isEmpty()) {
                    _state.update {
                        it.copy(
                            date = it.date.copy(
                                isErr = true,
                                errText = UiText.StringResource(R.string.error_empyt_BDate)
                            )
                        )
                    }

                    return
                }

                if (_state.value.isMakingApiCall || _state.value.date.isErr) return

                _state.update {
                    it.copy(
                        isMakingApiCall = true
                    )
                }

                viewModelScope.launch {
                    when (val result = repo.setBDate(_state.value.date.value)) {
                        is Result.Error -> {
                            when (result.error) {
                                DataError.Network.NO_INTERNET -> _uiEvent.send(
                                    SetBDateUiEvent.EmitToast(
                                        UiText.StringResource(R.string.error_no_internet)
                                    )
                                )

                                else -> _uiEvent.send(
                                    SetBDateUiEvent.EmitToast(
                                        UiText.StringResource(R.string.error_something_went_wrong)
                                    )
                                )
                            }
                        }

                        is Result.Success -> {
                            _uiEvent.send(SetBDateUiEvent.OnSuccess)
                        }
                    }

                    _state.update {
                        it.copy(
                            isMakingApiCall = false
                        )
                    }
                }
            }
        }
    }
}