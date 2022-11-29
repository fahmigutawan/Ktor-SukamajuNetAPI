package com.example.routing

import com.example.data.table.admin
import com.example.model.receive_request.LoginRequest
import com.example.model.send_response.LoginResponse
import com.example.model.send_response.MetaResponse
import com.example.model.table.customer
import com.example.util.TokenManager
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.mainRoute() {
    get("/") {
        call.respondText("Welcome to Sukamaju Net API Service")
    }
}

fun Route.userLoginRoute() {
    post("/user_login") {
        val body = call.receive<LoginRequest>()
        val username = body.username

        transaction {
            customer.select { customer.username eq username }.limit(1).firstOrNull()
        }?.let {
            val result = customer.toCustomer(it)

            if (result.password == body.password) {
                call.respond(
                    LoginResponse(
                        MetaResponse(
                            "true",
                            ""
                        ),
                        TokenManager.generateJwtToken(result.username, result.user_id)
                    )
                )
                return@post
            } else {
                call.respond(
                    LoginResponse(
                        MetaResponse(
                            "false",
                            "Password Salah"
                        ),
                        ""
                    )
                )
                return@post
            }
        }

        call.respond(
            LoginResponse(
                MetaResponse(
                    "false",
                    "Username tidak ditemukan"
                ),
                ""
            )
        )
    }
}

fun Route.adminLoginRoute() {
    post("/admin_login") {
        val body = call.receive<LoginRequest>()
        val username = body.username

        transaction {
            admin.select { admin.username eq username }.limit(1).firstOrNull()
        }?.let {
            val result = admin.toAdmin(it)

            if (result.password == body.password) {
                call.respond(
                    LoginResponse(
                        MetaResponse(
                            "true",
                            ""
                        ),
                        TokenManager.generateJwtToken(result.username, result.user_id)
                    )
                )
                return@post
            } else {
                call.respond(
                    LoginResponse(
                        MetaResponse(
                            "false",
                            "Password Salah"
                        ),
                        ""
                    )
                )
                return@post
            }
        }

        call.respond(
            LoginResponse(
                MetaResponse(
                    "false",
                    "Username tidak ditemukan"
                ),
                ""
            )
        )
    }
}