package com.example.data.table

import com.example.data.model.AdminModel
import com.example.data.model.CustomerModel
import com.example.model.table.customer
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object admin:Table() {
    val user_id = varchar("admin_id", 20)
    val pegawai_id = varchar("pegawai_id", 5)
    val username = varchar("username", 20)
    val password = varchar("password", 20)

    fun toAdmin(row: ResultRow): AdminModel =
        AdminModel(
            user_id = row[user_id],
            pegawai_id = row[pegawai_id],
            username = row[username],
            password = row[password]
        )
}