package com.example.config
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt


fun Application.configureSecurity() {
    val jwtConfig = JwtConfig(environment)

    authentication {
        // 🔒 Eén realm, maar we controleren dynamisch op rol
        jwt("auth-jwt") {
            realm = "AppRealm"
            verifier(
                JWT
                    .require(jwtConfig.algorithm)
                    .withAudience(jwtConfig.audience)
                    .withIssuer(jwtConfig.issuer)
                    .build()
            )

            validate { credential ->
                val audienceOk = credential.payload.audience.contains(jwtConfig.audience)
                val role = credential.payload.getClaim("role").asString()

                // Alleen accepteren als audience klopt én er een geldige rol is
                if (audienceOk && (role == "User" || role == "Admin")) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}



