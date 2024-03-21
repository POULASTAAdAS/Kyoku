package com.poulastaa.data.model.auth.auth_response

import com.poulastaa.data.model.EndPoints
import com.poulastaa.utils.Constants.ARTIST_IMAGE_ROOT_DIR
import com.poulastaa.utils.Constants.SERVICE_URL
import kotlinx.serialization.Serializable

@Serializable
data class ResponseArtist(
    val id: Int = 0,
    val name: String = "",
    val imageUrl: String = ""
) {
    companion object {
        fun getArtistImageUrl(profilePicUrl: String): String {
            return "${SERVICE_URL}${EndPoints.GetArtistImageUrl.route}?name=${
                profilePicUrl.replace(ARTIST_IMAGE_ROOT_DIR, "")
                    .removeSuffix("/")
                    .replace(" ", "_")
            }"
        }
    }
}
