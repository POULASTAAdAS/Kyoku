package com.poulastaa.common.database.entity.relation

import com.poulastaa.common.database.entity.AlbumArtistEntity
import com.poulastaa.common.database.entity.AlbumEntity
import com.poulastaa.common.database.entity.ArtistEntity
import com.poulastaa.common.database.entity.ComposerEntity
import com.poulastaa.common.database.entity.CountryEntity
import com.poulastaa.common.database.entity.GenreEntity
import com.poulastaa.common.database.entity.SongEntity

/**
 * Write model for one song and everything it links to.
 *
 * An empty relation list means the response did not carry that relation, so the existing links
 * are kept instead of being cleared.
 */
data class SongWithRelationsBundle(
    val song: SongEntity,
    val artists: List<ArtistEntity>,
    val albums: List<AlbumEntity>,
    val albumOnlyArtists: List<ArtistEntity>,
    val albumArtists: List<AlbumArtistEntity>,
    val genres: List<GenreEntity>,
    val countries: List<CountryEntity>,
    val composers: List<ComposerEntity>,
)
