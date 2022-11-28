package com.example.init

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.request.*
import org.slf4j.event.Level

fun Application.configureNegotiation(){
    install(ContentNegotiation){
        gson()
    }
}

fun Application.configureLogging(){
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
}