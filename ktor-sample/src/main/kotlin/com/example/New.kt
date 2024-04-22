package com.example

import io.ktor.network.tls.certificates.*
import java.io.*
import java.time.LocalDate
import java.time.temporal.TemporalAccessor
import java.util.Stack

fun main() {
    val stack = Stack<String>()

    stack.push("hello1")
    stack.push("hello2")
    stack.push("hello3")


    stack.pop()

    println(stack)
}