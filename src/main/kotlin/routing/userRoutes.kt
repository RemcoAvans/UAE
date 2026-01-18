package routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.baseRouter.BaseRouter.badRequest
import com.example.baseRouter.BaseRouter.handle
import com.example.baseRouter.BaseRouter.sendToken
import com.example.baseRouter.BaseRouter.unauthorized
import com.example.config.JwtConfig
import com.example.dtos.userDtos.userLoginDto
import com.example.repository.IUserRepository
import com.example.usecase.user.LoginUserUseCase

import dtos.UserRegisterDto
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respondText

import io.ktor.server.routing.*
import repository.UserRepository
import usecase.GetUsersUseCase
import usecase.user.DeleteUserUseCase
import usecase.user.RegisterAdminUseCase
import usecase.user.RegisterUseCase
import java.util.Date

fun Route.userRoutes(repo: IUserRepository) {
//    val registerUseCase = RegisterUseCase(repo)
    val getUsersUseCase = GetUsersUseCase(repo)
    val registerUseCase = RegisterUseCase(repo)
    val loginUserUseCase = LoginUserUseCase(repo)
    val registerAdminUseCase = RegisterAdminUseCase(repo)
    val deleteUserUseCase = DeleteUserUseCase(repo)



    post("/users/register") {
        val user = call.receive<UserRegisterDto>()
        val result = registerUseCase.execute(user)

        // If registration successful, generate and return JWT token
        if (result.success) {
            val jwt = JwtConfig(call.application.environment)
            val expiresAt = System.currentTimeMillis() + 600 * 1000
            val token = JWT.create()
                .withAudience(jwt.audience)
                .withIssuer(jwt.issuer)
                .withClaim("username", result.result?.username)
                .withClaim("role", result.result?.role)
                .withExpiresAt(Date(expiresAt))
                .sign(Algorithm.HMAC256(jwt.secret))
            call.sendToken(token, result.result?.username, expiresAt)
        } else {
            call.handle(result)
        }
    }

    post("/users/registerAdmin") {
        val user = call.receive<UserRegisterDto>()
        val result = registerAdminUseCase.execute(user)
        call.handle(result)
    }


    post("/login") {

        val logindata = call.receive<userLoginDto>()
        val result = loginUserUseCase.execute(logindata)

        if (result.success) {

            val jwt = JwtConfig(call.application.environment)

            val expiresAt = System.currentTimeMillis() + 600 * 1000
            val token =  JWT.create()
                .withAudience(jwt.audience)
                .withIssuer(jwt.issuer)
                .withClaim("username", result.result?.username)
                .withClaim("role", result.result?.role)
                .withExpiresAt(Date(expiresAt)) // 60 seconden
                .sign(Algorithm.HMAC256(jwt.secret))
            call.sendToken(token, result.result?.username, expiresAt)
        } else {
            call.handle(result)
        }






    }


        authenticate("auth-jwt"){
            get("/meAsUser") {
                val principal = call.principal<JWTPrincipal>()
                if (principal == null) {
                    call.unauthorized("Unauthenticated user")
                    return@get
                }

                val username = principal.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token expires at $expiresAt ms.")
            }

            get("/meAsAdmin/") {
                val principal = call.principal<JWTPrincipal>()
                if (principal == null) {
                    call.unauthorized("Unauthenticated user")
                    return@get
                }
                val role = principal.payload.getClaim("role").asString()
                if (role == "Admin"){
                    val username = principal.payload.getClaim("username").asString()
                    val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                    call.respondText("Hello, $username! you're role is : $role And you're Token expires at $expiresAt ms.")

                } else {
                    call.unauthorized()
                }



            }

            delete("/DeleteUser/{id}") {
                val principal = call.principal<JWTPrincipal>()
                if (principal == null) {
                    call.unauthorized("Unauthenticated user")
                    return@delete
                }
                val role = principal.payload.getClaim("role").asString()
                if (role == "Admin"){
                    val idParam = call.parameters["id"]
                    val id = idParam?.toInt()

                    val result = deleteUserUseCase.execute(id)
                    call.handle(result)


                } else {
                    call.unauthorized()
                }


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