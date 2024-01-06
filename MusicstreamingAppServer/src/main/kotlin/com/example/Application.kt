package com.example

import com.example.plugins.configureDatabase
import com.example.plugins.configureKoin
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.server.application.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

val invalidTokenList = ArrayList<String>()

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
    removeFirstTokenAfter10Minute()
}

fun Application.module() {
    configureKoin()
    configureSerialization()
    configureDatabase()
    configureRouting()
}

private fun removeFirstTokenAfter10Minute() {
    runBlocking {
        while (true) {
            delay(600000L)
            try {
                invalidTokenList.removeAt(0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}