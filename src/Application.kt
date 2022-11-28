package com.example

import com.example.init.configureLogging
import com.example.init.configureNegotiation
import com.example.routing.configureRouting
import io.ktor.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    configureLogging()
    configureNegotiation()
    configureRouting()
}

