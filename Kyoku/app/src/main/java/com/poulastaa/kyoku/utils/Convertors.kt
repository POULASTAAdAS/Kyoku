package com.poulastaa.kyoku.utils

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.gson.JsonParser
import com.poulastaa.kyoku.data.model.api.auth.email.EmailLogInReq
import com.poulastaa.kyoku.data.model.api.auth.email.EmailSignUpReq
import com.poulastaa.kyoku.data.model.api.auth.google.GoogleAuthReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.CreatePasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.GetPasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyAuthReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyJson
import com.poulastaa.kyoku.data.model.api.service.ResponseArtist
import com.poulastaa.kyoku.data.model.api.service.ResponseSong
import com.poulastaa.kyoku.data.model.api.service.artist.ViewArtist
import com.poulastaa.kyoku.data.model.api.service.home.AlbumPreview
import com.poulastaa.kyoku.data.model.api.service.home.ArtistSong
import com.poulastaa.kyoku.data.model.api.service.home.SongPreview
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.StoreArtistReq
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.SuggestArtistResponse
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.UiArtist
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.StoreGenreReq
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.SuggestGenreResponse
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.UiGenre
import com.poulastaa.kyoku.data.model.database.ArtistPrevResult
import com.poulastaa.kyoku.data.model.database.table.AlbumSongTable
import com.poulastaa.kyoku.data.model.database.table.ArtistMixTable
import com.poulastaa.kyoku.data.model.database.table.ArtistTable
import com.poulastaa.kyoku.data.model.database.table.DailyMixTable
import com.poulastaa.kyoku.data.model.database.table.FavouriteSongTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistSongTable
import com.poulastaa.kyoku.data.model.database.table.RecentlyPlayedPrevTable
import com.poulastaa.kyoku.data.model.database.table.prev.ArtistSongTable
import com.poulastaa.kyoku.data.model.database.table.prev.PlayingQueueTable
import com.poulastaa.kyoku.data.model.database.table.prev.PreviewAlbumTable
import com.poulastaa.kyoku.data.model.database.table.prev.ReqAlbumSongTable
import com.poulastaa.kyoku.data.model.home_nav_drawer.QueueSong
import com.poulastaa.kyoku.data.model.screens.auth.email.login.EmailLogInState
import com.poulastaa.kyoku.data.model.screens.auth.email.signup.EmailSignUpState
import com.poulastaa.kyoku.data.model.screens.auth.root.RootAuthScreenState
import com.poulastaa.kyoku.data.model.screens.home.HomeAlbumUiPrev
import com.poulastaa.kyoku.data.model.screens.home.HomeUiSongPrev
import com.poulastaa.kyoku.data.model.screens.player.PlayerSong
import com.poulastaa.kyoku.data.model.screens.song_view.UiPlaylistSong
import com.poulastaa.kyoku.data.model.screens.view_aritst.ViewArtistUiArtist
import com.poulastaa.kyoku.ui.theme.dark_type_two_background
import com.poulastaa.kyoku.ui.theme.dark_type_two_onBackground
import com.poulastaa.kyoku.ui.theme.dark_type_two_tertiary
import com.poulastaa.kyoku.utils.Constants.AUTH_TYPE_EMAIL_LOG_IN
import com.poulastaa.kyoku.utils.Constants.AUTH_TYPE_EMAIL_SIGN_UP
import com.poulastaa.kyoku.utils.Constants.AUTH_TYPE_GOOGLE
import com.poulastaa.kyoku.utils.Constants.AUTH_TYPE_PASSKEY
import com.poulastaa.kyoku.utils.Constants.TYPE_EMAIL_LOG_IN_REQ
import com.poulastaa.kyoku.utils.Constants.TYPE_EMAIL_SIGN_UP_REQ
import com.poulastaa.kyoku.utils.Constants.TYPE_GOOGLE_AUTH_REQ
import com.poulastaa.kyoku.utils.Constants.TYPE_PASSKEY_AUTH_REQ
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

fun RootAuthScreenState.toPasskeyAuthRequest() = PasskeyAuthReq(
    type = TYPE_PASSKEY_AUTH_REQ,
    authType = AUTH_TYPE_PASSKEY,
    email = this.passkeyEmail,
    displayName = this.passkeyEmail.removeSuffix("@gmail.com")
)

fun String.toPasskeyJson(): PasskeyJson {
    val json = JsonParser().parse(this).asJsonObject

    val type = json["type"].asString
    json.remove("type")

    val token = json["token"].asString
    json.remove("token")

    val challenge = json["challenge"].asString.removeSuffix("==")

    return PasskeyJson(
        type = type,
        challenge = challenge,
        req = json.toString(),
        token = token
    )
}

fun String.toCreatePasskeyUserReq(
    email: String,
    token: String,
    countryCode: String
) =
    CreatePasskeyUserReq(
        id = this,
        email = email,
        userName = email.removeSuffix("@gmail.com"),
        token = token,
        countryCode = countryCode
    )


fun String.toGetPasskeyUserReq(token: String) = GetPasskeyUserReq(
    id = this,
    token = token
)


fun String.toGoogleAuthReq(countryCode: String): GoogleAuthReq = GoogleAuthReq(
    type = TYPE_GOOGLE_AUTH_REQ,
    authType = AUTH_TYPE_GOOGLE,
    tokenId = this,
    countryCode = countryCode
)

fun EmailSignUpState.toEmailSignUpReq(countryCode: String) = EmailSignUpReq(
    type = TYPE_EMAIL_SIGN_UP_REQ,
    authType = AUTH_TYPE_EMAIL_SIGN_UP,
    email = this.email,
    password = this.password,
    userName = this.userName,
    countryCode = countryCode
)

fun EmailLogInState.toEmailLogInReq() = EmailLogInReq(
    type = TYPE_EMAIL_LOG_IN_REQ,
    authType = AUTH_TYPE_EMAIL_LOG_IN,
    email = this.email,
    password = this.password
)

fun EmailLogInState.toEmailLogInReq(email: String, password: String) = EmailLogInReq(
    type = TYPE_EMAIL_LOG_IN_REQ,
    authType = AUTH_TYPE_EMAIL_LOG_IN,
    email = email,
    password = password
)

fun ResponseSong.toPlaylistSongTable() = PlaylistSongTable(
    songId = this.id,
    coverImage = this.coverImage,
    masterPlaylistUrl = this.masterPlaylistUrl,
    totalTime = this.totalTime,
    title = this.title,
    artist = this.artist,
    album = this.album,
    date = this.date
)

fun ResponseSong.toAlbumTableEntry() = AlbumSongTable(
    songId = this.id,
    coverImage = this.coverImage,
    masterPlaylistUrl = this.masterPlaylistUrl,
    totalTime = this.totalTime,
    title = this.title,
    artist = this.artist,
    album = this.album,
    date = this.date
)

fun SuggestGenreResponse.toUiGenreList(): List<UiGenre> = this.genreList.map {
    UiGenre(
        name = it,
        isSelected = false
    )
}

fun Iterable<UiGenre>.toGenreNameList(): List<String> = this.mapNotNull {
    if (it.isSelected) it.name else null
}

fun List<String>.toStoreGenreReq() = StoreGenreReq(
    data = this
)

fun Iterable<UiGenre>.toAlreadySendGenreList(): List<String> = this.map { it.name }


fun SuggestArtistResponse.toUiArtistList() = this.artistList.map {
    UiArtist(
        name = it.name,
        profileUrl = it.imageUrl,
        isSelected = false
    )
}

fun Iterable<UiArtist>.toAlreadySendArtistList() = this.map {
    it.name
}


fun Iterable<UiArtist>.toArtistNameList() = this.mapNotNull {
    if (it.isSelected) it.name else null
}

fun List<String>.toStoreArtistReq() = StoreArtistReq(
    data = this
)


fun List<PreviewAlbumTable>.toHomeAlbumUiPrev() = this.map {
    HomeAlbumUiPrev(
        id = it.albumId,
        name = it.name,
        coverImage = it.coverImage
    )
}


fun List<AlbumPreview>.toAlbumTablePrevEntry() = this.map {
    PreviewAlbumTable(
        albumId = it.id,
        name = it.name,
        coverImage = it.coverImage
    )
}

fun ResponseArtist.toArtistTableEntry() = ArtistTable(
    artistId = this.id,
    name = this.name,
    coverImage = this.imageUrl
)

fun List<ResponseSong>.toFavouriteTableEntryList() = this.map {
    FavouriteSongTable(
        songId = it.id,
        coverImage = it.coverImage,
        masterPlaylistUrl = it.masterPlaylistUrl,
        totalTime = it.totalTime,
        title = it.title,
        artist = it.artist,
        album = it.album,
        date = it.date
    )
}

fun ResponseSong.toFavouriteTableEntry() = FavouriteSongTable(
    songId = this.id,
    coverImage = this.coverImage,
    masterPlaylistUrl = this.masterPlaylistUrl,
    totalTime = this.totalTime,
    title = this.title,
    artist = this.artist,
    album = this.album,
    date = this.date
)


fun ArtistSong.toArtistSongEntry() = ArtistSongTable(
    songId = this.songId,
    title = this.title,
    coverImage = this.coverImage
)

fun List<SongPreview>.toHistoryPrevSongEntry() = this.map {
    RecentlyPlayedPrevTable(
        songId = it.id.toLong(),
        title = it.title,
        coverImage = it.coverImage
    )
}


fun ArtistPrevResult.toHomeUiSongPrev() = HomeUiSongPrev(
    id = this.songId,
    title = this.title,
    coverImage = this.coverImage,
    artist = this.name
)

fun List<ResponseSong>.toDailyMixEntry() = this.map {
    DailyMixTable(
        songId = it.id,
        title = it.title,
        artist = it.artist,
        album = it.album,
        coverImage = it.coverImage,
        masterPlaylistUrl = it.masterPlaylistUrl,
        totalTime = it.totalTime,
        year = it.date
    )
}

fun List<ResponseSong>.toArtistMixEntry() = this.map {
    ArtistMixTable(
        songId = it.id,
        title = it.title,
        artist = it.artist,
        album = it.album,
        coverImage = it.coverImage,
        masterPlaylistUrl = it.masterPlaylistUrl,
        totalTime = it.totalTime,
        year = it.date
    )
}

fun List<ResponseSong>.toReqAlbumEntry(
    albumId: Long,
    albumName: String
) = this.map {
    ReqAlbumSongTable(
        albumId = albumId,
        albumName = albumName,
        songId = it.id,
        coverImage = it.coverImage,
        masterPlaylistUrl = it.masterPlaylistUrl,
        title = it.title,
        artist = it.artist,
        totalTime = it.totalTime,
        date = it.date
    )
}

fun List<ReqAlbumSongTable>.toUiPlaylistSong() = this.map {
    UiPlaylistSong(
        songId = it.songId,
        title = it.title,
        artist = it.artist,
        coverImage = it.coverImage,
        masterPlaylistUrl = it.masterPlaylistUrl,
        totalTime = it.totalTime
    )
}

@JvmName("ResponseSongToPlayingQueueTable")
fun ResponseSong.toPlayingQueueTable() = PlayingQueueTable(
    songId = this.id,
    title = this.title,
    artist = this.artist,
    album = this.album,
    coverImage = this.coverImage,
    masterPlaylistUrl = this.masterPlaylistUrl,
    totalTime = this.totalTime,
    year = this.date
)

@JvmName("PlayerSongToPlayingQueueTable")
fun List<PlayerSong>.toPlayingQueueTable() = this.map {
    PlayingQueueTable(
        songId = it.id,
        title = it.title,
        artist = it.artist.toString().trimStart('[').trimEnd(']'),
        album = it.album,
        coverImage = it.url,
        masterPlaylistUrl = it.masterPlaylist,
        totalTime = it.totalInMili.toString(),
        colorOne = String.format("#%06X", 0xFFFFFF and it.colorOne.toArgb()),
        colorTwo = String.format("#%06X", 0xFFFFFF and it.colorTwo.toArgb())
    )
}

fun List<PlayingQueueTable>.toPlayerDataIfAny() = this.map { song ->
    val bitmap = BitmapConverter.decodeToBitmap(song.coverImage)
    val palette = bitmap?.let { PaletteGenerator.extractColorFromBitMap(it) }

    var lightMuted = palette?.get(ColorType.LIGHT_MUTED)
    var darkMuted = palette?.get(ColorType.DARK_MUTED)

    val backupOne = palette?.get(ColorType.LIGHT_VIBRANT)
    val backupTwo = palette?.get(ColorType.DARK_VIBRANT)
    val colorThree = palette?.get(ColorType.MUTED_SWATCH)

    if (lightMuted == darkMuted) {
        lightMuted = backupOne
        darkMuted = backupTwo
    }

    val lightColor = lightMuted?.let { Color(parseColor(it)) } ?: dark_type_two_tertiary
    val darkColor = darkMuted?.let { Color(parseColor(it)) } ?: dark_type_two_background
    val thirdColor = colorThree?.let { Color(parseColor(it)) } ?: dark_type_two_onBackground

    QueueSong(
        isPlaying = false,
        playerSong = PlayerSong(
            id = song.songId,
            url = song.coverImage,
            masterPlaylist = song.masterPlaylistUrl,
            title = song.title,
            artist = song.artist.split(","),
            totalTime = String.format("%.2f", (song.totalTime.toDouble() / 60000)),
            totalInMili = song.totalTime.toFloat(),
            colorOne = lightColor,
            colorTwo = darkColor,
            colorThree = thirdColor
        )
    )
}

@JvmName("PlayingQueueTableToPlayerData")
suspend fun List<PlayingQueueTable>.toPlayerData() = coroutineScope {
    this@toPlayerData.map { song ->
        async {
            val bitmap = BitmapConverter.decodeToBitmap(song.coverImage)
            val palette = bitmap?.let { PaletteGenerator.extractColorFromBitMap(it) }

            var lightMuted = palette?.get(ColorType.LIGHT_MUTED)
            var darkMuted = palette?.get(ColorType.DARK_MUTED)

            val backupOne = palette?.get(ColorType.LIGHT_VIBRANT)
            val backupTwo = palette?.get(ColorType.DARK_VIBRANT)
            val colorThree = palette?.get(ColorType.MUTED_SWATCH)

            if (lightMuted == darkMuted) {
                lightMuted = backupOne
                darkMuted = backupTwo
            }

            val lightColor = lightMuted?.let { Color(parseColor(it)) } ?: dark_type_two_tertiary
            val darkColor = darkMuted?.let { Color(parseColor(it)) } ?: dark_type_two_background
            val thirdColor = colorThree?.let { Color(parseColor(it)) } ?: dark_type_two_onBackground

            QueueSong(
                isPlaying = false,
                playerSong = PlayerSong(
                    id = song.songId,
                    url = song.coverImage,
                    masterPlaylist = song.masterPlaylistUrl,
                    title = song.title,
                    artist = song.artist.split(","),
                    totalTime = String.format("%.2f", (song.totalTime.toDouble() / 60000)),
                    totalInMili = song.totalTime.toFloat(),
                    colorOne = lightColor,
                    colorTwo = darkColor,
                    colorThree = thirdColor
                )
            )
        }
    }.awaitAll()
}

@JvmName("UiPlaylistSongToPlayerData")
suspend fun List<UiPlaylistSong>.toPlayerData() = coroutineScope {
    this@toPlayerData.map { song ->
        async {
            val bitmap = BitmapConverter.decodeToBitmap(song.coverImage)
            val palette = bitmap?.let { PaletteGenerator.extractColorFromBitMap(it) }

            var lightMuted = palette?.get(ColorType.LIGHT_MUTED)
            var darkMuted = palette?.get(ColorType.DARK_MUTED)

            val backupOne = palette?.get(ColorType.LIGHT_VIBRANT)
            val backupTwo = palette?.get(ColorType.DARK_VIBRANT)
            val colorThree = palette?.get(ColorType.MUTED_SWATCH)

            if (lightMuted == darkMuted) {
                lightMuted = backupOne
                darkMuted = backupTwo
            }

            val lightColor = lightMuted?.let { Color(parseColor(it)) } ?: dark_type_two_tertiary
            val darkColor = darkMuted?.let { Color(parseColor(it)) } ?: dark_type_two_background
            val thirdColor = colorThree?.let { Color(parseColor(it)) } ?: dark_type_two_onBackground

            QueueSong(
                isPlaying = false,
                playerSong = PlayerSong(
                    id = song.songId,
                    url = song.coverImage,
                    masterPlaylist = song.masterPlaylistUrl,
                    title = song.title,
                    artist = song.artist.split(","),
                    totalTime = String.format("%.2f", (song.totalTime.toDouble() / 60000)),
                    totalInMili = song.totalTime.toFloat(),
                    colorOne = lightColor,
                    colorTwo = darkColor,
                    colorThree = thirdColor
                )
            )
        }
    }.awaitAll()
}

fun List<ViewArtist>.toViewArtist() = this.map {
    ViewArtistUiArtist(
        artistId = it.id,
        name = it.name,
        coverImage = it.coverImage,
        listened = it.points
    )
}
