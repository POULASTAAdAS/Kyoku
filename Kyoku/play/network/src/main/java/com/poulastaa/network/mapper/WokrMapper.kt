package com.poulastaa.network.mapper

import com.poulastaa.core.data.model.PlaylistDto
import com.poulastaa.core.domain.model.Playlist
import com.poulastaa.core.domain.model.PlaylistWithSong
import com.poulastaa.core.domain.model.SyncData
import com.poulastaa.network.model.SyncDto


fun PlaylistDto.toPlaylistWithSong() = PlaylistWithSong(
    playlist = Playlist(
        id = this.id,
        name = this.name
    ),
    songs = this.listOfSong.map { it.toSong() }
)

fun <T, R> SyncDto<T>.toSyncData(
    convert: (T) -> R
): SyncData<R> {
    return SyncData(
        removeIdList = this.removeIdList,
        newAlbumList = this.newAlbumList.map(convert)
    )
}