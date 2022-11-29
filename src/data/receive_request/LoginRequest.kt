package com.example.model.receive_request

import com.example.model.send_response.MetaResponse

data class LoginRequest(
    val username:String,
    val password:String
)
