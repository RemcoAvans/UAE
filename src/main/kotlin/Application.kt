package com.example



import ai.koog.ktor.Koog
import com.example.config.configureSecurity
import io.ktor.server.application.*

// omdat we nu met een conf file werken is dit voldoende
fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

//
//fun main() {
//    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
//        .start(wait = true)
//
//}

fun Application.module() {

    install(Koog)
    configureSecurity()
    configureSerialization()
    configureHTTP()
    configureRouting()

}
