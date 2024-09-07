package com.poulastaa.core.domain.model

enum class CreatePlaylistType(val value: String) {
    RECENT_HISTORY("Recent History"),
    YOUR_FAVOURITES("Your Favorites"),
    SUGGESTED_FOR_YOU("Suggested For You"),
    YOU_MAY_ALSO_LIKE("You may also like"),
    SEARCH("Search")
}