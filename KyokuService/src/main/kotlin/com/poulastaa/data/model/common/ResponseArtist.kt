package com.poulastaa.data.model.common

import com.poulastaa.utils.Constants.BASE_URL
import kotlinx.serialization.Serializable

@Serializable
data class ResponseArtist(
    val id: Int = 0,
    val name: String = "",
    val imageUrl: String = ""
) {
    companion object {
        fun getArtistImageUrl(name: String): String {
            return "${BASE_URL}${EndPoints.GetArtistImageUrl.route}?name=$name"
        }
    }
}
