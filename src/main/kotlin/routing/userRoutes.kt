package routing

import com.example.baseRouter.BaseRouter.handle
import com.example.usecase.CreateUserUseCase
import dtos.UserRegisterDto
import io.ktor.server.request.receive
import io.ktor.server.routing.*
import model.User
import repository.UserRepository
import usecase.GetUsersUseCase

fun Route.userRoutes() {
    val repo = UserRepository()
//    val registerUseCase = RegisterUseCase(repo)
    val getUsersUseCase = GetUsersUseCase(repo)
    val createUsersUseCase = CreateUserUseCase(repo)


    post("/users/register") {
//        val user = call.receive<UserRegisterDto>()
//        val result = registerUseCase.execute(user)
//        call.handle(result)
    }

    post("/users/login") {
//        val credentials = call.receive<LoginDto>()
//        val loginResult = loginUseCase.execute(credentials)
//
//        if (loginResult.isSuccess) {
//            val token = jwtService.generateToken(loginResult.data!!)
//            call.respond(mapOf("token" to token))
//        } else {
//            call.handle(loginResult)
//        }
    }

    get("/users") {
        val users = getUsersUseCase.execute()
        call.handle(users)
    }

    get("/users/new") {
//        val user = User(1, "Jaron")
//        val result =  createUsersUseCase.execute(user)
//        call.handle(result)
    }
}