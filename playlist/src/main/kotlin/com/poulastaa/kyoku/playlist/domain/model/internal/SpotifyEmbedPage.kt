package com.poulastaa.kyoku.playlist.domain.model.internal

import com.google.gson.annotations.SerializedName

/**
 * `__NEXT_DATA__` json of the public Spotify embed page (`open.spotify.com/embed/playlist/{id}`).
 * Path: `props.pageProps.state.data.entity`.
 */
data class SpotifyEmbedPage(
    @SerializedName("props")
    val props: Props? = null,
) {
    data class Props(
        @SerializedName("pageProps")
        val pageProps: PageProps? = null,
    )

    data class PageProps(
        @SerializedName("state")
        val state: State? = null,
    )

    data class State(
        @SerializedName("data")
        val data: Data? = null,
    )

    data class Data(
        @SerializedName("entity")
        val entity: SpotifyEmbedEntity? = null,
    )
}

data class SpotifyEmbedEntity(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("trackList")
    val trackList: List<SpotifyEmbedTrack>? = null,
)

data class SpotifyEmbedTrack(
    @SerializedName("title")
    val title: String? = null,
    /** comma separated artist names */
    @SerializedName("subtitle")
    val subtitle: String? = null,
)
