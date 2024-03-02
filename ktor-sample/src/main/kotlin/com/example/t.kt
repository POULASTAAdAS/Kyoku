package com.example

import kotlinx.coroutines.*

suspend fun main() {
    CoroutineScope(Dispatchers.IO).launch {
        val one = async {
            println(1)
        }

        val two = async {
            delay(200)
            println(2)
        }

        val three = async {
            println(3)

        }


        one.await()
        two.await()
        three.await()
    }.join()
}
