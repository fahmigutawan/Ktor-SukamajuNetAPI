package com.example.util

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlin.reflect.KClass

object RouteService {
    fun <T : Any> Route.post(path:String, onReceived:(T) -> Unit, type:KClass<T>){
        post(path) {
            val body = call.receive(type)

            onReceived(body)
        }
    }

    fun <T: Any> Route.get(path:String, type:KClass<T>){
        get(path) {

        }
    }
}