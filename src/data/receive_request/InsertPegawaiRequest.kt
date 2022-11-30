package com.example.data.receive_request

data class InsertPegawaiRequest(
    val profile_pic: String,
    val salary: Double,
    val nama: String,
    val no_telp: String,
    val jalan: String,
    val kode_pos: String,
    val kota: String,
    val provinsi: String
)