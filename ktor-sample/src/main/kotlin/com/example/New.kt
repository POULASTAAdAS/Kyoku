package com.example

import kotlin.random.Random

fun main() {
    val random = Random(10).nextInt(30,40)

    println(random)
}