package com.example

data class Item(
    val id: Int,
    val name: String
)

fun <T> Iterable<T>.toIdList(): List<Int> {
    val list = ArrayList<Int>()

    this.forEach {
        it as Item
        list.add(it.id)
    }

    return list
}

private fun String.toSpotifyPlaylistId(): String =
    this.removePrefix("https://open.spotify.com/playlist/").split("?si=")[0]


fun main() {
    val map = HashMap<Int, Item>()

    for (i in 1..10) {
        map[i] = Item(id = i, name = "name$i")
    }

//    println(map.values.toIdList())

    val spotifyUrl = "https://open.spotify.com/playlist/0cPmYAURUsJJQlB6XT5nEc?si=b9f6a535285743a9"
    val playlistId = spotifyUrl.toSpotifyPlaylistId()

    println("Playlist ID: $playlistId")
}