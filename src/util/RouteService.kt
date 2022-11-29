package com.example.util

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlin.reflect.KClass

    fun <T : Any> Route.routePost(path:String, onReceived:(T) -> Unit, type:KClass<T>){
        post(path) {
            val body = call.receive(type)

            onReceived(body)
        }
    }

    fun <T: Any> Route.routeGet(path:String, type:KClass<T>){
        get(path) {

        }
    }