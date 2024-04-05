package com.example

import java.sql.SQLIntegrityConstraintViolationException
import kotlin.random.Random

fun main() {
    val random = SQLIntegrityConstraintViolationException().errorCode

    println(random)
}