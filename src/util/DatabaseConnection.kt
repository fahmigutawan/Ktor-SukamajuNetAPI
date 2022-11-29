package com.example.util

import io.ktor.application.*
import io.ktor.util.pipeline.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

suspend fun connectToDatabase(onError: suspend (SQLException) -> Unit, onConnect: suspend (Connection) -> Unit) {
    try {
        val conn = DriverManager.getConnection(DbUrl.url)
        onConnect(conn)
    } catch (e: SQLException) {
        onError(e)
    }
}