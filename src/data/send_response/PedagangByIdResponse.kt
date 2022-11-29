package com.example.data.send_response

import com.example.data.model.PedagangModel
import com.example.model.send_response.MetaResponse

data class PedagangByIdResponse(
    val metaResponse: MetaResponse,
    val data: PedagangModel?
)