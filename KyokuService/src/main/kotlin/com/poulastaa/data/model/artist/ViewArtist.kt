package com.poulastaa.data.model.artist

import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.utils.Constants
import com.poulastaa.utils.Constants.BASE_URL
import kotlinx.serialization.Serializable

@Serializable
data class ViewArtist(
    val id: Long,
    val name: String,
    val coverImage: String,
    val points: Long
) {
    companion object {
        fun getArtistImageUrl(profilePicUrl: String): String {
            return "$BASE_URL${EndPoints.GetArtistImageUrl.route}?name=${
                profilePicUrl.replace(Constants.ARTIST_IMAGE_ROOT_DIR, "")
                    .removeSuffix("/")
                    .replace(" ", "_")
            }"
        }
    }
}
