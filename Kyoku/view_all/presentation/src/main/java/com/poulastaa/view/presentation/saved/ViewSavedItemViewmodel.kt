package com.poulastaa.view.presentation.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.view.domain.model.DtoViewSavedItemNavigationType
import com.poulastaa.view.domain.model.ViewSavedAllowedNavigationScreen
import com.poulastaa.view.domain.repository.ViewSavedItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ViewSavedItemViewmodel @Inject constructor(
    private val repo: ViewSavedItemRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ViewSavedUiState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ViewSavedUiState()
    )

    fun init(type: ViewSavedUiItemType) {
        _state.update {
            it.copy(
                type = type
            )
        }

        loadData(type)
    }

    private val _uiEvent = Channel<ViewSavedUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: ViewSavedUiAction) {
        when (action) {
            ViewSavedUiAction.OnAddNewItemClick -> {
                when (_state.value.type) {
                    ViewSavedUiItemType.ARTIST -> TODO()

                    ViewSavedUiItemType.PLAYLIST -> _state.update {
                        it.copy(
                            isNewPlaylistDialogOpen = it.isNewPlaylistDialogOpen.not()
                        )
                    }

                    ViewSavedUiItemType.ALBUM -> TODO()

                    else -> return
                }
            }

            is ViewSavedUiAction.OnNewPlaylistCreated -> viewModelScope.launch {
                _uiEvent.send(
                    ViewSavedUiEvent.Navigate(
                        ViewSavedAllowedNavigationScreen.Other(
                            otherId = action.playlistId,
                            type = DtoViewSavedItemNavigationType.PLAYLIST
                        )
                    )
                )
            }

            ViewSavedUiAction.OnEditToggle -> {
                _state.update {
                    it.copy(
                        isEditEnabled = it.isEditEnabled.not()
                    )
                }
            }

            ViewSavedUiAction.OnDeleteAllClick -> {

            }

            is ViewSavedUiAction.OnItemClick -> when (action.clickType) {
                ItemClickType.CLICK -> viewModelScope.launch {
                    _uiEvent.send(
                        ViewSavedUiEvent.Navigate(
                            screen = when (_state.value.type) {
                                ViewSavedUiItemType.ARTIST -> ViewSavedAllowedNavigationScreen.ExploreArtist(
                                    artistId = action.id
                                )

                                ViewSavedUiItemType.PLAYLIST -> ViewSavedAllowedNavigationScreen.Other(
                                    otherId = action.id,
                                    type = DtoViewSavedItemNavigationType.PLAYLIST
                                )

                                ViewSavedUiItemType.ALBUM -> ViewSavedAllowedNavigationScreen.Other(
                                    otherId = action.id,
                                    type = DtoViewSavedItemNavigationType.ALBUM
                                )

                                ViewSavedUiItemType.NONE -> throw IllegalArgumentException("Invalid ViewSavedUiItem Type")
                            }
                        )
                    )
                }

                ItemClickType.LONG_CLICK -> TODO("implement long click")
            }
        }
    }

    private fun loadData(type: ViewSavedUiItemType) = viewModelScope.launch {
        val data = repo.loadSavedData(type.toDtoViewSavedItemType())

        _state.update {
            it.copy(
                loadingType = LoadingType.Content,
                items = data.map { it.toViewSavedUiItem() }
            )
        }
    }
}