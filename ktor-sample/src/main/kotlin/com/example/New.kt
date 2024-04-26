package com.example

fun main() {
    val currentMile = 17971
    val totalMinute = 423947.30

//    println(currentMile.toFloat() / totalMinute.toFloat() * 100f)

    val temp = (totalMinute * 38.346474 / 100f).toLong()
    println(temp)
}


