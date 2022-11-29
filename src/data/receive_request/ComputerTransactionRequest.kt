package com.example.data.receive_request

data class ComputerTransactionRequest(
    val komputer_id:String,
    val customer_id:String,
    val harga:Int
)