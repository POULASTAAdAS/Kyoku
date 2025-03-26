package com.poulastaa.main.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.main.domain.repository.HomeRepository
import com.poulastaa.main.domain.repository.work.RefreshScheduler
import com.poulastaa.main.domain.repository.work.SyncLibraryScheduler
import com.poulastaa.main.presentation.components.UiMainViewMoreItemType
import com.poulastaa.main.presentation.home.mapper.toNavigateToView
import com.poulastaa.main.presentation.home.mapper.toNavigateToViewHome
import com.poulastaa.main.presentation.home.mapper.toUiHomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class HomeViewmodel @Inject constructor(
    private val repo: HomeRepository,
    private val work: SyncLibraryScheduler,
    private val refresh: RefreshScheduler,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds), // 5 seconds
        initialValue = HomeUiState()
    )

    private val _uiEvent = Channel<HomeUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun init(isInitial: Boolean) {
        viewModelScope.launch {
            loadData()
            if (isInitial && _state.value.staticData.suggestedArtist.isEmpty()) {
                repo.getHome()
                loadData()
            }
        }
    }

    init {
        work.scheduleSync(15.minutes)
        refresh.scheduleRefresh()
    }

    fun onAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.OnSavedItemCLick -> viewModelScope.launch {
                when (action.clickType) {
                    ItemClickType.CLICK -> when (action.type) {
                        null -> return@launch
                        else -> _uiEvent.send(action.type.toNavigateToViewHome(action.id))
                    }

                    ItemClickType.LONG_CLICK -> when (action.type) {
                        null -> return@launch
                        else -> TODO("Implement long click for OnSavedItemCLick on home screen")
                    }
                }
            }

            is HomeUiAction.OnExploreTypeItemClick -> viewModelScope.launch {
                when (action.clickType) {
                    ItemClickType.CLICK -> _uiEvent.send(action.type.toNavigateToView())
                    ItemClickType.LONG_CLICK -> TODO("Implement long click for OnExploreTypeItemClick on home screen")
                }
            }

            is HomeUiAction.OnViewMore -> viewModelScope.launch {
                when (action.type) {
                    UiMainViewMoreItemType.SUGGESTED_ARTIST_IN_DETAIL,
                    UiMainViewMoreItemType.SUGGESTED_ALBUM_IN_DETAIL,
                        -> TODO("Implement view more for ${action.type} on home screen i.e. Explore Artist")

                    else -> _uiEvent.send(action.type.toNavigateToView(action.id))
                }
            }

            is HomeUiAction.OnSuggestedArtistLongClick -> {}
            is HomeUiAction.OnSuggestedAlbumLongClick -> {}
            is HomeUiAction.OnSuggestArtistSongClick -> {}
        }
    }

    private suspend fun loadData() {
        val home = repo.loadData()

        if (home.savedItems.isEmpty()) return
        _state.update {
            home.toUiHomeState(home.savedItems.random(Random))
        }
    }
}