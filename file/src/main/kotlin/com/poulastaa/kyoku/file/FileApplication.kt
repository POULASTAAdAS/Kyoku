package com.poulastaa.kyoku.file

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class FileApplication

fun main(args: Array<String>) {
    runApplication<FileApplication>(*args)
}
