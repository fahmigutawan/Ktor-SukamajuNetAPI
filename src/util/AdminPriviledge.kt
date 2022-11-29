package com.example.util

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.util.pipeline.*
import java.sql.SQLException

suspend fun PipelineContext<Unit, ApplicationCall>.adminPriviledge(onFailed:suspend (SQLException) -> Unit,status:suspend (AdminStatus) -> Unit){
    val user_id = call.principal<JWTPrincipal>()!!.payload.getClaim("user_id").asString()

    connectToDatabase(
        onError = {
            onFailed(it)
        },
        onConnect = {
            val query = "select count(*) as count from admin where pegawai_id=?"
            val statement= it.prepareStatement(query)
            statement.setString(1, user_id)
            val res = statement.executeQuery()

            if(res.next()){
                if(res.getInt("count") > 0){
                    status(AdminStatus.Admin())
                }else{
                    status(AdminStatus.User())
                }
            }else{
                status(AdminStatus.User())
            }
        }
    )
}