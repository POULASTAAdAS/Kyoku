package com.example

fun main() {
    val mileSecString = "329783.329"
    val mileSec = mileSecString.toDouble()

    val minutes = String.format("%.2f", (mileSec / 60000))

    println(minutes)
}