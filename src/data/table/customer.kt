package com.example.data.table

import com.example.data.model.CustomerModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object customer:Table() {
    val username = varchar("username", 20)
    val customer_id = varchar("customer_id", 20)
    val password = varchar("password", 20)

    fun toCustomerModel(row:ResultRow) =
        CustomerModel(
            row[username],
            row[customer_id],
            row[password]
        )
}