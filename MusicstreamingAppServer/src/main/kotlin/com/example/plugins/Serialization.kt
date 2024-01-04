package com.example.plugins

import com.example.data.model.DefaultAuthModel
import com.example.data.model.EmailLoginReq
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
