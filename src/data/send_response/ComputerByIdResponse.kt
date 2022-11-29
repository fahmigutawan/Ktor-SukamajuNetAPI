package com.example.data.send_response

import com.example.data.model.ComputerModel
import com.example.model.send_response.MetaResponse

data class ComputerByIdResponse(
    val metaResponse: MetaResponse,
    val data:ComputerModel?
)
