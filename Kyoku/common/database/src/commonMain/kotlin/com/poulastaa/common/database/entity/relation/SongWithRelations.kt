package com.poulastaa.common.database.entity.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.poulastaa.common.database.entity.AlbumEntity
import com.poulastaa.common.database.entity.ArtistEntity
import com.poulastaa.common.database.entity.ComposerEntity
import com.poulastaa.common.database.entity.CountryEntity
import com.poulastaa.common.database.entity.GenreEntity
import com.poulastaa.common.database.entity.SongAlbumEntity
import com.poulastaa.common.database.entity.SongArtistEntity
import com.poulastaa.common.database.entity.SongComposerEntity
import com.poulastaa.common.database.entity.SongCountryEntity
import com.poulastaa.common.database.entity.SongEntity
import com.poulastaa.common.database.entity.SongGenreEntity

data class SongWithRelations(
    @Embedded
    val song: SongEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SongArtistEntity::class,
            parentColumn = "songId",
            entityColumn = "artistId",
        ),
    )
    val artists: List<ArtistEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SongAlbumEntity::class,
            parentColumn = "songId",
            entityColumn = "albumId",
        ),
    )
    val albums: List<AlbumEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SongGenreEntity::class,
            parentColumn = "songId",
            entityColumn = "genreId",
        ),
    )
    val genres: List<GenreEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SongCountryEntity::class,
            parentColumn = "songId",
            entityColumn = "countryId",
        ),
    )
    val countries: List<CountryEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SongComposerEntity::class,
            parentColumn = "songId",
            entityColumn = "composerId",
        ),
    )
    val composers: List<ComposerEntity>,
)
