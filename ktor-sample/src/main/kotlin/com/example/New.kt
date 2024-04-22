package com.example

import io.ktor.network.tls.certificates.*
import java.io.*
import java.time.LocalDate
import java.time.temporal.TemporalAccessor

fun main() {
    val date = "2024-04-20"

    val new = LocalDate.parse(date)


    println(new)
}