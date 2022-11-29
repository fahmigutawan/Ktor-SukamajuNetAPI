package com.example.data.send_response

import com.example.data.model.CustomerInfoModel
import com.example.data.model.PegawaiInfoModel
import com.example.model.send_response.MetaResponse

data class PegawaiInfoResponse(
    val metaResponse: MetaResponse,
    val data:PegawaiInfoModel?
)