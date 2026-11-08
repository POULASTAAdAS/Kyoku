package com.poulastaa.setup.viewmodels.import_playlist

import androidx.lifecycle.viewModelScope
import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.viewmodels.BaseViewmodel
import com.poulastaa.common.viewmodels.SnackbarAction
import com.poulastaa.common.viewmodels.SnackbarType
import com.poulastaa.common.viewmodels.states.UiTextFiledState
import com.poulastaa.common.viewmodels.stringEmptyPlaylistLink
import com.poulastaa.common.viewmodels.stringImportPlaylistAnyway
import com.poulastaa.common.viewmodels.stringInvalidPlaylistLink
import com.poulastaa.common.viewmodels.stringPlaylistAlreadyImported
import com.poulastaa.common.viewmodels.stringPlaylistImported
import com.poulastaa.common.viewmodels.stringPlaylistNotFound
import com.poulastaa.setup.domain.SetupRepository
import com.poulastaa.setup.viewmodels.utils.normalizedLink
import com.poulastaa.setup.viewmodels.utils.spotifyPlaylistIdOrNull
import kotlinx.coroutines.launch

class ImportPlaylistViewmodel(
    private val repo: SetupRepository,
) : BaseViewmodel<ImportPlaylistUiState, ImportPlaylistUiAction, Nothing>(
    initialSate = ImportPlaylistUiState(),
) {
    init {
        loadSavedPlaylists()
    }

    private fun loadSavedPlaylists() {
        viewModelScope.launch {
            val playlists = when (val result = repo.getSavedPlaylists()) {
                is AppResult.Error -> emptyList()
                is AppResult.Success -> result.response.map { it.toUiPlaylist() }
            }

            updateState {
                copy(
                    isLoading = false,
                    // stored playlists first, then anything imported while they were loading
                    playlists = playlists + this.playlists.filterNot { imported ->
                        playlists.any { it.id == imported.id }
                    },
                )
            }
        }
    }

    override suspend fun handleAction(action: ImportPlaylistUiAction) {
        when (action) {
            is ImportPlaylistUiAction.OnLinkChange -> updateState {
                copy(link = UiTextFiledState(value = action.link))
            }

            is ImportPlaylistUiAction.OnPlaylistToggle -> updateState {
                copy(
                    playlists = playlists.map {
                        if (it.id == action.playlistId) it.copy(isExpanded = !it.isExpanded)
                        else it
                    }
                )
            }

            ImportPlaylistUiAction.OnImportClick -> importPlaylist(force = false)
            ImportPlaylistUiAction.OnForceImportClick -> importPlaylist(force = true)
        }
    }

    private suspend fun importPlaylist(force: Boolean) {
        if (_uiState.value.isMakingApiCall) return

        val link = _uiState.value.link.value.normalizedLink()

        if (link.isEmpty()) return setLinkError(stringEmptyPlaylistLink())

        val spotifyPlaylistId = link.spotifyPlaylistIdOrNull()
            ?: return setLinkError(stringInvalidPlaylistLink())

        updateState {
            copy(
                isMakingApiCall = true,
                link = UiTextFiledState(value = link),
            )
        }

        when (val result = repo.importPlaylist(spotifyPlaylistId, force)) {
            is AppResult.Error -> {
                updateState { copy(isMakingApiCall = false) }

                when (result.error.error) {
                    ApiError.Network.NOT_FOUND -> showSnackbar(
                        stringPlaylistNotFound(),
                        SnackbarType.Error
                    )

                    ApiError.Playlist.PLAYLIST_ALREADY_IMPORTED -> showSnackbar(
                        message = stringPlaylistAlreadyImported(),
                        type = SnackbarType.Info,
                        action = SnackbarAction(label = stringImportPlaylistAnyway()) {
                            onAction(ImportPlaylistUiAction.OnForceImportClick)
                        },
                    )

                    else -> if (handleCommonError(result.error.error).not()) showSnackbar(
                        result.error.message,
                        SnackbarType.Error
                    )
                }
            }

            is AppResult.Success -> {
                val playlist = result.response.toUiPlaylist()

                updateState {
                    copy(
                        isMakingApiCall = false,
                        link = UiTextFiledState(),
                        playlists = playlists.filterNot { it.id == playlist.id } + playlist,
                    )
                }

                showSnackbar(stringPlaylistImported(), SnackbarType.Success)
            }
        }
    }

    private fun setLinkError(message: String) {
        updateState { copy(link = link.copy(isError = true, errorMessage = message)) }
        showSnackbar(message, SnackbarType.Error)
    }
}
