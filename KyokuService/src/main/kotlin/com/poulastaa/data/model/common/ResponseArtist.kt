package com.poulastaa.data.model.common

import com.poulastaa.utils.Constants
import com.poulastaa.utils.Constants.BASE_URL
import kotlinx.serialization.Serializable

@Serializable
data class ResponseArtist(
    val id: Int = 0,
    val name: String = "",
    val imageUrl: String = ""
) {
    companion object {
        fun getArtistImageUrl(profilePicUrl: String): String {
            return "${BASE_URL}${EndPoints.GetArtistImageUrl.route}?name=${
                profilePicUrl.replace(Constants.ARTIST_IMAGE_ROOT_DIR, "")
                    .removeSuffix("/")
                    .replace(" ", "_")
            }"
        }
    }
}
