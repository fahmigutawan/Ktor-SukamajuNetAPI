package com.example.routing

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {
    routing {
        mainRoute()
    }
}

private fun Route.mainRoute() {
    get("/") {
        call.respondText("Welcome to Sukamaju Net API Service")
    }
}