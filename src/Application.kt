package com.example

import com.example.init.*
import com.example.util.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    configureMSSQL()
    configureJWT()
    configureLogging()
    configureNegotiation()
    regularRouting()
    authRouting()
}

