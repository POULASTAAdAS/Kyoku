package com.poulastaa.data.dao

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.table.ArtistTable
import com.poulastaa.utils.Constants.ARTIST_IMAGE_ROOT_DIR
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ArtistDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ArtistDao>(ArtistTable)

    val name by ArtistTable.name
    private val profilePic by ArtistTable.profilePicUrl
    val points by ArtistTable.points

    fun constructProfilePic() = this.profilePic?.let {
        "${System.getenv("SERVICE_URL") + EndPoints.GetArtistImage.route}?artistCover=${
            it.replace(ARTIST_IMAGE_ROOT_DIR, "").replace(" ", "_")
        }"
    }
}