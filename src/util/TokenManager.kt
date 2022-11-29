package com.example.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import io.ktor.auth.jwt.*
import io.ktor.config.*

object TokenManager {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val secret = config.property("secret").getString()
    val issuer = config.property("issuer").getString()
    val audience = config.property("audience").getString()
    val jwtRealm = config.property("jwtRealm").getString()
    private val algorithm = Algorithm.HMAC256(secret)
    private val verifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateJwtToken(username:String, user_id:String):String{
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("username", username)
            .withClaim("user_id", user_id)
            .sign(algorithm)
    }

    fun configureKtorFeature(config: JWTAuthenticationProvider.Configuration) = with(config) {
        verifier(verifier)
        realm = jwtRealm
        validate { credential ->
            JWTPrincipal(credential.payload)
        }
    }
}