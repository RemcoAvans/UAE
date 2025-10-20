package com.example.config


import io.ktor.server.application.*
import com.auth0.jwt.algorithms.Algorithm

class JwtConfig(environment: ApplicationEnvironment) {
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val secret = environment.config.property("jwt.secret").getString()
    val algorithm = Algorithm.HMAC256(secret)
}
