package routing

import com.example.baseRouter.BaseRouter.handle
import com.example.usecase.CreateUserUseCase
import io.ktor.server.routing.*
import model.User
import repository.UserRepository
import usecase.GetUsersUseCase

fun Route.userRoutes() {
    val repo = UserRepository()
    val getUsersUseCase = GetUsersUseCase(repo)
    val createUsersUseCase = CreateUserUseCase(repo)

    get("/users") {
        val users = getUsersUseCase.execute()
        call.handle(users)
    }

    get("/users/new") {
        val user = User(1, "Jaron")
        val result =  createUsersUseCase.execute(user)
        call.handle(result)
    }
}