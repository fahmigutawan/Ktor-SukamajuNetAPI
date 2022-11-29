package com.example.data.model

import com.example.data.table.pegawai_information

data class PegawaiInfoModel(
    val pegawai_id: String,
    val profile_pic: String,
    val salary_acc_number: String,
    val salary: Double,
    val nama: String,
    val no_telp: String,
    val jalan: String,
    val kode_pos: String,
    val kota: String,
    val provinsi: String
)
