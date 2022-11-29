package com.example.init

import com.example.routing.*
import com.example.util.DbUrl
import com.example.util.TokenManager
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.Database
import org.slf4j.event.Level

fun Application.configureNegotiation() {
    install(ContentNegotiation) {
        gson()
    }
}

fun Application.configureLogging() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
}

fun Application.configureJWT() {
    install(Authentication) {
        jwt("auth-jwt") {
            TokenManager.configureKtorFeature(this)
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}

fun configureMSSQL() {
    Database.connect(
        url = DbUrl.url,
        driver = "org.h2.Driver"
    )
}

fun Application.regularRouting() {
    routing {
        mainRoute()
        userLoginRoute()
        adminLoginRoute()
    }
}

fun Application.authRouting() {
    routing {
        authenticate("auth-jwt") {
            getUserInfo()
            getUserInfoById()
            getAdminInfo()
            getPegawaiById()
            getComputerById()
            getComputersList()
            getFoodsList()
            getFoodById()
        }
    }
}

fun Application.configureCORS(){
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.AccessControlAllowHeaders)
        header(HttpHeaders.ContentType)
        header(HttpHeaders.AccessControlAllowOrigin)
        allowCredentials = true
        anyHost()
        header(HttpHeaders.Authorization)
        header(HttpHeaders.ContentType)
        header(HttpHeaders.AccessControlAllowOrigin)
        header(HttpHeaders.AccessControlAllowHeaders)
        header(HttpHeaders.AccessControlExposeHeaders)
        header(HttpHeaders.AccessControlRequestHeaders)
    }
}