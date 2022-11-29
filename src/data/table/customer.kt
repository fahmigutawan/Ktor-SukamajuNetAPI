package com.example.model.table

import com.example.data.model.CustomerModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object customer:Table(){
    val user_id = varchar("user_id", 8)
    val username = varchar("username", 20)
    val password = varchar("password", 20)

    fun toCustomer(row:ResultRow):CustomerModel =
        CustomerModel(
            user_id = row[user_id],
            username = row[username],
            password = row[password]
        )
}
