package com.example.data.send_response

import com.example.data.model.FoodModel
import com.example.model.send_response.MetaResponse

data class FoodByIdResponse(
    val metaResponse: MetaResponse,
    val data: FoodModel?
)
