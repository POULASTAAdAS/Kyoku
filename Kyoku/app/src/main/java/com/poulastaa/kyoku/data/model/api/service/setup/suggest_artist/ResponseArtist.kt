package com.poulastaa.data.model.setup.artist

import com.poulastaa.data.model.EndPoints
import com.poulastaa.utils.Constants.BASE_URL
import kotlinx.serialization.Serializable

@Serializable
data class ResponseArtist(
    val id: Int,
    val name: String,
    val imageUrl: String
) {
    companion object {
        fun getArtistImageUrl(name: String): String {
            return "${BASE_URL}${EndPoints.GetArtistImageUrl.route}?name=$name"
        }
    }
}
