package com.example.data.send_response

import com.example.data.model.CustomerInfoModel
import com.example.model.send_response.MetaResponse

data class CustomerInfoResponse(
    val metaResponse: MetaResponse,
    val data:CustomerInfoModel?
)