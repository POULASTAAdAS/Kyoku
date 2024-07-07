package com.poulastaa.data.mappers

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.utils.Constants.ARTIST_IMAGE_ROOT_DIR
import com.poulastaa.utils.Constants.COVER_IMAGE_ROOT_DIR

fun String.constructSongCoverImage() = "${System.getenv("SERVICE_URL") + EndPoints.GetCoverImage.route}?coverImage=${
    this.replace(COVER_IMAGE_ROOT_DIR, "")
}"

fun String.constructArtistProfileUrl() = "${
    System.getenv("SERVICE_URL") + EndPoints.GetArtistImage.route
}?artistCover=${
    this.replace(ARTIST_IMAGE_ROOT_DIR, "").replace(" ", "_")
}"