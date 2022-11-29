package com.example.data.table

import com.example.data.model.PegawaiInfoModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object pegawai_information:Table() {
    val pegawai_id = varchar("pegawai_id", 20)
    val profile_pic = varchar("profile_pic", 128)
    val salary_acc_number = varchar("salary_acc_number", 20).nullable()
    val salary = decimal("salary", 20,2)
    val nama = varchar("nama", 40)
    val no_telp = varchar("no_telp", 20)
    val jalan = varchar("jalan", 20)
    val kode_pos = varchar("kode_pos",20)
    val kota = varchar("kota", 20)
    val provinsi = varchar("provinsi", 20)

    fun toPegawaiInfoModel(row:ResultRow) =
        PegawaiInfoModel(
            row[pegawai_id],
            row[profile_pic],
            row[salary_acc_number] ?: "",
            row[salary].toDouble(),
            row[nama],
            row[no_telp],
            row[jalan],
            row[kode_pos],
            row[kota],
            row[provinsi]
        )
}