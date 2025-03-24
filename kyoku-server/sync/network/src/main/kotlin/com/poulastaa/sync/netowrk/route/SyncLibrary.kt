package com.poulastaa.sync.netowrk.route

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.sync.domain.repository.SynRepository
import com.poulastaa.sync.netowrk.mapper.toDtoSyncData
import com.poulastaa.sync.netowrk.mapper.toDtoSyncType
import com.poulastaa.sync.netowrk.mapper.toResponseSyncPlaylistSong
import com.poulastaa.sync.netowrk.model.SyncPlaylistPayload
import com.poulastaa.sync.netowrk.model.SyncReq
import com.poulastaa.sync.netowrk.model.SyncType
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.syncLibrary(repo: SynRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.SyncLibrary.route) {
            post {
                val type = try {
                    SyncType.valueOf(
                        call.parameters["type"] ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)
                    )
                } catch (_: Exception) {
                    return@post call.respondRedirect(EndPoints.UnAuthorized.route)
                }

                when (type) {
                    SyncType.SYNC_PLAYLIST_SONGS -> {
                        val req = call.receiveNullable<SyncReq<SyncPlaylistPayload>>()
                            ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)
                        val result = repo.syncPlaylistSongs(
                            idList = req.idList.map {
                                Pair(
                                    first = it.playlistId,
                                    second = it.listOfSongId
                                )
                            }
                        ).toResponseSyncPlaylistSong()

                        call.respond(status = HttpStatusCode.OK, message = result)
                    }

                    else -> {
                        val req = call.receiveNullable<SyncReq<Long>>()
                            ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

                        val result = when (type) {
                            SyncType.SYNC_ALBUM -> repo.syncData<DtoFullAlbum>(
                                type = type.toDtoSyncType(),
                                savedIdList = req.idList
                            ).toDtoSyncData()

                            SyncType.SYNC_PLAYLIST -> repo.syncData<DtoFullPlaylist>(
                                type = type.toDtoSyncType(),
                                savedIdList = req.idList
                            ).toDtoSyncData()

                            SyncType.SYNC_ARTIST -> repo.syncData<DtoArtist>(
                                type = type.toDtoSyncType(),
                                savedIdList = req.idList
                            ).toDtoSyncData()

                            SyncType.SYNC_FAVOURITE -> repo.syncData<DtoSong>(
                                type = type.toDtoSyncType(),
                                savedIdList = req.idList
                            ).toDtoSyncData()

                            else -> return@post call.respondRedirect(EndPoints.UnAuthorized.route)
                        }

                        call.respond(status = HttpStatusCode.OK, message = result)
                    }
                }
            }
        }
    }
}