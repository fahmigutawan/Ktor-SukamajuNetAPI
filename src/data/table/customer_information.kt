package com.example.data.table

import com.example.data.model.CustomerInfoModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object customer_information:Table() {
    val customer_id = varchar("customer_id", 20)
    val nama = varchar("nama" ,40)
    val profile_pic = varchar("profile_pic", 128)
    val balance_acc = decimal("balance_acc", 20,2)

    fun toCustomerInfoModel(row:ResultRow) =
        CustomerInfoModel(
            row[customer_id],
            row[nama],
            row[profile_pic],
            row[balance_acc].toDouble()
        )
}