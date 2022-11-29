package com.example.data.table

import com.example.data.model.ComputerModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object komputer : Table() {
    val komputer_id = varchar("komputer_id", 5)
    val kategori_id = varchar("kategori_id", 5)
    val harga_per_jam = decimal("harga_perjam", 20, 2)

    fun toComputerModel(row: ResultRow) =
        ComputerModel(
            row[komputer_id],
            row[kategori_id],
            kategori_komputer.toKategoriModel(row),
            row[harga_per_jam].toString()
        )
}