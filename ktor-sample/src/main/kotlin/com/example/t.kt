package com.example

import java.io.File

fun main() {
    val path = "F:/songs/artist/Pawan Sing/"

    val folder = File(path)

    val image = folder.listFiles()?.random()

    println(image)
}
