package com.poulastaa.kyoku.discovery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@EnableCaching
@EnableEurekaServer
@SpringBootApplication
class DiscoveryApplication

fun main(args: Array<String>) {
    runApplication<DiscoveryApplication>(*args)
}
