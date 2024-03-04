package com.example

data class B(
    val s: String = "anshu",
    private val st: String = "nayan"
)


fun main() {
    println(B())
}