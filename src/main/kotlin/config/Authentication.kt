package com.example.config
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt


fun Application.configureSecurity() {
    val userJwt = JwtConfig(environment)
    val adminJwt = JwtConfig(environment)

    authentication {
        jwt("auth-jwt-user") {
            realm = "UsersRealm"
            verifier(
                JWT
                    .require(userJwt.algorithm)
                    .withAudience(userJwt.audience)
                    .withIssuer(userJwt.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(userJwt.audience)) JWTPrincipal(credential.payload) else null
            }
        }

        //EEN TEST REALM
        jwt("auth-jwt-admin") {
            realm = "AdminRealm"
            verifier(
                JWT
                    .require(adminJwt.algorithm) // eventueel een andere secret
                    .withAudience(adminJwt.audience)
                    .withIssuer(adminJwt.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(adminJwt.audience)) JWTPrincipal(credential.payload) else null
            }
        }

    }



}



