package com.example.routes.common

import com.example.data.model.GoogleUserSession
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.addSessionInterceptor() {
    intercept(ApplicationCallPipeline.Call) {
        val session = call.sessions.get<GoogleUserSession>()

        session?.let {
            call.sessions.set(it)
        }
    }
}