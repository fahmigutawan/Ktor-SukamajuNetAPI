package com.example.data.send_response

import com.example.data.model.PegawaiInfoModel
import com.example.model.send_response.MetaResponse

data class PegawaiInfoListResponse(
    val metaResponse: MetaResponse,
    val data:List<PegawaiInfoModel>
)
