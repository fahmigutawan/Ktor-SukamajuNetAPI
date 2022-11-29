package com.example.util

import com.example.data.model.*
import java.sql.ResultSet

object ResultSetConvert {
    fun toUserInfo(res: ResultSet) =
        CustomerInfoModel(
            customer_id = res.getString("customer_id") ?: "",
            nama = res.getString("nama") ?: "",
            profile_pic = res.getString("profile_pic") ?: "",
            balance_acc = res.getDouble("balance_acc") ?: 0.0
        )

    fun toPegawaiInfo(res: ResultSet) =
        PegawaiInfoModel(
            pegawai_id = res.getString("pegawai_id") ?: "",
            profile_pic = res.getString("profile_pic") ?: "",
            salary_acc_number = res.getString("salary_acc_number") ?: "",
            salary = res.getDouble("salary") ?: .0,
            nama = res.getString("nama") ?: "",
            no_telp = res.getString("no_telp") ?: "",
            jalan = res.getString("jalan") ?: "",
            kode_pos = res.getString("kode_pos") ?: "",
            kota = res.getString("kota") ?: "",
            provinsi = res.getString("provinsi") ?: ""
        )

    fun toComputerModel(res: ResultSet) =
        ComputerModel(
            komputer_id = res.getString("komputer_id") ?: "",
            kategori_id = res.getString("kategori_id") ?: "",
            kategori = CategoryModel(
                kategori_id = res.getString("kategori_id") ?: "",
                kategori_word = res.getString("kategori_word") ?: ""
            ),
            harga_perjam = res.getString("harga_perjam") ?: "",
            status = res.getString("status") ?: ""
        )

    fun toFoodModel(res: ResultSet) =
        FoodModel(
            makanan_id = res.getString("makanan_id") ?: "",
            pedagang_id = res.getString("pedagang_id") ?: "",
            harga = res.getDouble("harga") ?: .0,
            stok = res.getInt("stok") ?: 0,
            nama = res.getString("nama") ?: "",
            kategori_id = res.getString("kategori_id") ?: "",
            kategori = CategoryModel(
                kategori_id = res.getString("kategori_id") ?: "",
                kategori_word = res.getString("kategori_word") ?: ""
            )
        )

    fun toPedagangInfo(res: ResultSet) =
        PedagangModel(
            pedagang_id = res.getString("pedagang_id") ?: "",
            stand_name = res.getString("stand_name") ?: "",
            stand_number = res.getString("stand_number") ?: ""
        )
}