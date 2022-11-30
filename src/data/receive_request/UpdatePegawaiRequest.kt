package com.example.data.receive_request

data class UpdatePegawaiRequest(
    val pegawai_id:String,
    val profile_pic: String,
    val salary: Double,
    val nama: String,
    val no_telp: String,
    val jalan: String,
    val kode_pos: String,
    val kota: String,
    val provinsi: String
)