package routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.baseRouter.BaseRouter.badRequest
import com.example.baseRouter.BaseRouter.handle
import com.example.baseRouter.BaseRouter.sendToken
import com.example.config.JwtConfig

import dtos.UserRegisterDto
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respondText

import io.ktor.server.routing.*
import repository.UserRepository
import usecase.GetUsersUseCase
import usecase.user.RegisterUseCase
import java.util.Date

fun Route.userRoutes() {
    val repo = UserRepository()
//    val registerUseCase = RegisterUseCase(repo)
    val getUsersUseCase = GetUsersUseCase(repo)
    val registerUseCase = RegisterUseCase(repo)



    post("/users/register") {
        val user = call.receive<UserRegisterDto>()
        val result = registerUseCase.execute(user)
        call.handle(result)
    }


    get("/login") {
        val jwt = JwtConfig(environment)
        val expiresAt = System.currentTimeMillis() + 60 * 1000
        val token =  JWT.create()
            .withAudience(jwt.audience)
            .withIssuer(jwt.issuer)
            .withClaim("username", "Jaron Freijser")
            .withExpiresAt(Date(expiresAt)) // 60 seconden
            .sign(Algorithm.HMAC256(jwt.secret))
        call.sendToken(token, "Jaron", expiresAt)




    }


        authenticate("auth-jwt-user"){
            get("/meAsUser") {
                val principal = call.principal<JWTPrincipal>()
                if (principal == null) {
                    call.badRequest("Unauthenticated user")
                    return@get
                }

                val username = principal.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token expires at $expiresAt ms.")
            }
        }



    get("/users") {
        val users = getUsersUseCase.execute()
        call.handle(users)
    }

    get("/users/new") {
//        val user = User(1, "Jaron")
//        val result =  createUsersUseCase.execute(user)
        //call.handle(result)
    }
}