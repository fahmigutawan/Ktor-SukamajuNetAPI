package com.example.data.send_response

import com.example.data.model.FoodModel
import com.example.model.send_response.MetaResponse

data class FoodListResponse(
    val metaResponse: MetaResponse,
    val data: List<FoodModel>
)
