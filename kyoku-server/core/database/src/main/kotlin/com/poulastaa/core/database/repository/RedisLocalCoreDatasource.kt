package com.poulastaa.core.database.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*
import com.poulastaa.core.domain.repository.auth.Email
import redis.clients.jedis.JedisPool

class RedisLocalCoreDatasource(
    private val redisPool: JedisPool,
    private val gson: Gson,
) : LocalCoreCacheDatasource, RedisKeys() {
    // user
    override fun cacheUsersByEmail(email: String, type: UserType): DtoDBUser? =
        cacheSingleValue<Email, DtoDBUser>(Group.USER, "${type.name}:$email")

    override fun setUserByEmail(key: Email, type: UserType, value: DtoDBUser) =
        setSingleValueWithExp(Group.USER, "${type.name}:$key", value)

    // playlist
    override fun setPlaylistById(playlistDto: DtoPlaylist) =
        setSingleValueWithExp(Group.PLAYLIST, playlistDto.id, playlistDto)

    // song
    override fun setSongById(song: DtoSong) = setSingleValueWithExp(Group.SONG, song.id, song)
    override fun setSongById(list: List<DtoSong>) = setMultipleValueWithExp(Group.SONG, list.associateBy { it.id })

    override fun cacheSongById(songId: SongId): DtoSong? = cacheSingleValue<SongId, DtoSong>(Group.SONG, songId)

    override fun cacheSongById(list: List<SongId>): List<DtoSong> =
        cacheMultipleValue<SongId, DtoSong>(Group.SONG, list)

    // genre
    override fun cacheGenreById(genreId: GenreId): DtoGenre? = cacheSingleValue<GenreId, DtoGenre>(Group.GENRE, genreId)
    override fun cacheGenreById(list: List<GenreId>): List<DtoGenre> =
        cacheMultipleValue<GenreId, DtoGenre>(Group.GENRE, list)

    override fun setGenreById(genre: DtoGenre) = setSingleValueWithExp(Group.GENRE, genre.id, genre)
    override fun setGenreById(list: List<DtoGenre>) = setMultipleValueWithExp(Group.GENRE, list.associateBy { it.id })

    // album
    override fun cacheAlbumById(albumId: AlbumId): DtoAlbum? = cacheSingleValue<AlbumId, DtoAlbum>(Group.ALBUM, albumId)

    override fun cacheAlbumById(list: List<AlbumId>): List<DtoAlbum> =
        cacheMultipleValue<AlbumId, DtoAlbum>(Group.ALBUM, list)

    override fun setAlbumById(album: DtoAlbum) = setSingleValueWithExp(Group.ALBUM, album.id, album)
    override fun setAlbumById(list: List<DtoAlbum>) = setMultipleValueWithExp(Group.ALBUM, list.associateBy { it.id })


    // artist
    override fun cacheArtistById(artistId: ArtistId): DtoArtist? =
        cacheSingleValue<ArtistId, DtoArtist>(Group.ARTIST, artistId)

    override fun cacheArtistById(list: List<ArtistId>): List<DtoArtist> =
        cacheMultipleValue<ArtistId, DtoArtist>(Group.ARTIST, list)

    override fun setArtistById(artist: DtoArtist) = setSingleValueWithExp(Group.ARTIST, artist.id, artist)
    override fun setArtistById(list: List<DtoArtist>) =
        setMultipleValueWithExp(Group.ARTIST, list.associateBy { it.id })


    // country
    override fun cacheCountryById(countryId: CountryId): DtoCountry? =
        cacheSingleValue<CountryId, DtoCountry>(Group.COUNTRY, countryId)

    override fun cacheCountryById(list: List<CountryId>): List<DtoCountry> =
        cacheMultipleValue<CountryId, DtoCountry>(Group.COUNTRY, list)

    override fun setCountryById(country: DtoCountry) = setSingleValueWithExp(Group.COUNTRY, country.id, country)
    override fun setCountryById(list: List<DtoCountry>) =
        setMultipleValueWithExp(Group.COUNTRY, list.associateBy { it.id })


    // Song Info
    override fun cacheSongInfo(songId: SongId): DtoSongInfo? =
        cacheSingleValue<SongId, DtoSongInfo>(Group.COUNTRY, songId)

    override fun cacheSongInfo(list: List<SongId>): List<DtoSongInfo> =
        cacheMultipleValue<SongId, DtoSongInfo>(Group.SONG_INFO, list)

    override fun setSongInfoById(songInfo: DtoSongInfo) = setSingleValueWithExp(Group.SONG_INFO, songInfo.id, songInfo)
    override fun setSongInfoById(list: List<DtoSongInfo>) =
        setMultipleValueWithExp(Group.SONG_INFO, list.associateBy { it.id })


    // Playlist
    override fun cachePlaylistOnId(playlistId: PlaylistId): DtoPlaylist? =
        cacheSingleValue<PlaylistId, DtoPlaylist>(Group.PLAYLIST, playlistId)

    override fun cachePlaylistOnId(list: List<PlaylistId>): List<DtoPlaylist> =
        cacheMultipleValue<PlaylistId, DtoPlaylist>(Group.PLAYLIST, list)

    override fun setPlaylistOnId(playlist: DtoPlaylist) = setSingleValueWithExp(Group.PLAYLIST, playlist.id, playlist)

    override fun setPlaylistOnId(list: List<DtoPlaylist>) =
        setMultipleValueWithExp(Group.PLAYLIST, list.associateBy { it.id })


    // relation songId artistId
    override fun cacheArtistIdBySongId(songId: SongId): List<ArtistId> = redisPool.resource.use { jedis ->
        jedis.get("${Group.RELATION_SONG_ARTIST}:$songId")?.let { list ->
            list.split(",").map { it.toLong() }
        } ?: emptyList()
    }

    override fun cacheArtistIdBySongId(list: List<SongId>): Map<SongId, List<ArtistId>> =
        redisPool.resource.use { jedis ->
            list.map { songId ->
                songId to (jedis.get("${Group.RELATION_SONG_ARTIST}:$songId")?.let { list ->
                    list.split(",").map { it.toLong() }
                } ?: emptyList())
            }
        }.toMap()

    override fun setArtistIdBySongId(
        songId: SongId,
        list: List<ArtistId>,
    ) = setSingleValueWithExp(Group.RELATION_SONG_ARTIST, songId, list.joinToString(","))

    override fun setArtistIdBySongId(map: Map<SongId, List<ArtistId>>) {
        redisPool.resource.use { jedis ->
            val pipeline = jedis.pipelined()

            map.forEach { (k, v) ->
                pipeline.setex(
                    "${Group.RELATION_SONG_ARTIST}:$k",
                    Group.GENRE.expTime,
                    gson.toJson(v.joinToString(","))
                )
            }

            pipeline.sync()
        }
    }


    // relation song genre
    override fun cacheGenreIdBySongId(songId: SongId): GenreId? =
        cacheSingleValue(Group.RELATION_SONG_GENRE, songId)?.toInt()

    override fun cacheGenreIdBySongId(list: List<SongId>): Map<SongId, GenreId> =
        cacheMultipleValue<SongId>(Group.RELATION_SONG_GENRE, list).map { it.key to it.value.toInt() }.toMap()

    override fun setGenreIdBySongId(songId: SongId, countryId: GenreId) =
        setSingleValueWithExp(Group.RELATION_SONG_GENRE, songId, countryId.toString())

    override fun setGenreIdBySongId(map: Map<SongId, GenreId>) =
        setMultipleValueWithExp(Group.RELATION_SONG_GENRE, map.mapValues { it.value.toString() })


    // relation song album
    override fun cacheAlbumIdBySongId(songId: SongId): AlbumId? =
        cacheSingleValue(Group.RELATION_SONG_ALBUM, songId)

    override fun cacheAlbumIdBySongId(list: List<SongId>): Map<SongId, AlbumId> =
        cacheMultipleValue<SongId>(Group.RELATION_SONG_ALBUM, list)

    override fun setAlbumIdBySongId(songId: SongId, albumId: AlbumId) =
        setSingleValueWithExp(Group.RELATION_SONG_ALBUM, songId, albumId.toString())

    override fun setAlbumIdBySongId(map: Map<SongId, AlbumId>) =
        setMultipleValueWithExp(Group.RELATION_SONG_ALBUM, map.mapValues { it.value.toString() })


    // relation artist genre
    override fun cacheGenreIdByArtistId(artistId: ArtistId): GenreId? =
        cacheSingleValue(Group.RELATION_ARTIST_GENRE, artistId)?.toInt()

    override fun cacheGenreIdByArtistId(list: List<ArtistId>): Map<ArtistId, GenreId> =
        cacheMultipleValue<ArtistId>(Group.RELATION_ARTIST_GENRE, list).map { it.key to it.value.toInt() }.toMap()

    override fun setGenreIdByArtistId(artistId: ArtistId, genreId: GenreId) =
        setSingleValueWithExp(Group.RELATION_ARTIST_GENRE, artistId, genreId.toString())

    override fun setGenreIdByArtistId(map: Map<GenreId, ArtistId>) =
        setMultipleValueWithExp(Group.RELATION_ARTIST_GENRE, map.mapValues { it.value.toString() })


    // relation artist country
    override fun cacheCountryIdByArtistId(artistId: ArtistId): CountryId? =
        cacheSingleValue(Group.RELATION_ARTIST_COUNTRY, artistId)?.toInt()

    override fun cacheCountryIdByArtistId(list: List<ArtistId>): Map<ArtistId, CountryId> =
        cacheMultipleValue<ArtistId>(Group.RELATION_ARTIST_COUNTRY, list).map { it.key to it.value.toInt() }.toMap()

    override fun setCountryIdByArtistId(artistId: ArtistId, countryId: CountryId) =
        setSingleValueWithExp(Group.RELATION_ARTIST_COUNTRY, artistId, countryId.toString())

    override fun setCountryIdByArtistId(map: Map<GenreId, CountryId>) =
        setMultipleValueWithExp(Group.RELATION_ARTIST_COUNTRY, map.mapValues { it.value.toString() })


    // set
    private fun <K, V> setSingleValueWithExp(group: Group, key: K, value: V) {
        redisPool.resource.use { jedis ->
            jedis.setex(
                "${group.name}:$key",
                group.expTime,
                gson.toJson(value)
            )
        }
    }

    private fun <K> setSingleValueWithExp(group: Group, key: K, value: String) {
        if (value.isBlank()) return

        redisPool.resource.use { jedis ->
            jedis.setex(
                "${group.name}:$key",
                group.expTime,
                value
            )
        }
    }

    @JvmName("setSingleValueWithExpGson")
    private fun <K, V> setMultipleValueWithExp(group: Group, map: Map<K, V>) {
        if (map.isEmpty()) return

        redisPool.resource.use { jedis ->
            val pipeline = jedis.pipelined()

            map.forEach { (k, v) ->
                pipeline.setex(
                    "${group.name}:$k",
                    Group.GENRE.expTime,
                    gson.toJson(v)
                )
            }

            pipeline.sync()
        }
    }

    private fun <K> setMultipleValueWithExp(group: Group, map: Map<K, String>) {
        if (map.isEmpty()) return

        redisPool.resource.use { jedis ->
            val pipeline = jedis.pipelined()

            map.forEach { (k, v) ->
                if (v.isNotBlank()) pipeline.setex(
                    "${group.name}:$k",
                    Group.GENRE.expTime,
                    v
                )
            }

            pipeline.sync()
        }
    }

    // get
    private inline fun <T, reified V> cacheSingleValue(group: Group, key: T) = redisPool.resource.use { jedis ->
        jedis.get("${group.name}:$key")
    }?.let {
        gson.fromJson(it, V::class.java)
    }

    private fun cacheSingleValue(group: Group, key: Any) = redisPool.resource.use { jedis ->
        jedis.get("${group.name}:$key")
    }?.toLong()

    private inline fun <T, reified V> cacheMultipleValue(group: Group, keys: List<T>) =
        redisPool.resource.use { jedis ->
            jedis.mget(*keys.map { "${group.name}:$it" }.toTypedArray())
                .mapNotNull { it }
                .map { gson.fromJson(it, V::class.java) }
        }

    private fun <T> cacheMultipleValue(group: Group, keys: List<T>) = redisPool.resource.use { jedis ->
        keys.map {
            it to jedis.get("${group.name}:$it")?.toLong()
        }.mapNotNull {
            if (it.second != null) it.first to it.second!!
            else null
        }
    }.toMap()
}