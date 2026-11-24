//
//  ImportPlaylistView.swift
//  iosApp
//
//  Created by Poulastaa Das on 22/11/26.
//

import ComposeApp

typealias ImportPlaylistViewModel = ObservableViewModel<
    ImportPlaylistUiState, any ImportPlaylistUiAction, KotlinNothing
>

extension ObservableViewModel
where
    State == ImportPlaylistUiState, Action == any ImportPlaylistUiAction,
    Event == KotlinNothing
{
    convenience init() {
        self.init {
            $0.importPlaylistViewmodel()
                as! BaseViewmodel<
                    State, any ImportPlaylistUiAction, KotlinNothing
                >
        }
    }

    // MARK: - Input

    func onLinkChange(_ link: String) {
        onAction(ImportPlaylistUiActionOnLinkChange(link: link))
    }

    // MARK: - Toggles

    func onPlaylistToggle(_ playlistId: Int64) {
        onAction(ImportPlaylistUiActionOnPlaylistToggle(playlistId: playlistId))
    }

    // MARK: - Submit

    func onSubmitClick() {
        onAction(ImportPlaylistUiActionOnImportClick.shared)
    }

    func onForceImportClick() {
        onAction(ImportPlaylistUiActionOnForceImportClick.shared)
    }
}
