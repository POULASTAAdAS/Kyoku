package com.poulastaa.kyoku

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform