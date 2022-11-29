package com.example.data.table

import com.example.data.model.AdminModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object admin:Table() {
    val pegawai_id = varchar("pegawai_id", 20)
    val username = varchar("username", 20)
    val password = varchar("password", 20)

    fun toAdminModel(row:ResultRow) =
        AdminModel(
            row[pegawai_id],
            row[username],
            row[password]
        )
}