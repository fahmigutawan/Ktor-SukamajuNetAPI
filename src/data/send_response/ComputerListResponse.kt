package com.example.data.send_response

import com.example.data.model.ComputerModel
import com.example.model.send_response.MetaResponse

data class ComputerListResponse(
    val metaResponse: MetaResponse,
    val data:List<ComputerModel>
)
