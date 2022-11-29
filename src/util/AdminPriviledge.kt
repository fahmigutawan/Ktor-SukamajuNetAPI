package com.example.util

import com.example.data.table.admin
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.util.pipeline.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

suspend fun PipelineContext<Unit, ApplicationCall>.adminPriviledge(status:(AdminStatus) -> Unit){
    val user_id = call.principal<JWTPrincipal>()!!.payload.getClaim("user_id").asString()

    val result = transaction {
        admin.select { admin.pegawai_id eq user_id }.firstOrNull()
    }

    when(result){
        null -> status(AdminStatus.User())
        else -> status(AdminStatus.Admin())
    }
}