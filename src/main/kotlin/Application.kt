package com.example

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.ext.agent.reActStrategy
import ai.koog.ktor.Koog
import ai.koog.ktor.aiAgent
import ai.koog.prompt.executor.clients.openai.OpenAILLMClient
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.executor.llms.MultiLLMPromptExecutor
import ai.koog.prompt.llm.LLMProvider
import com.example.config.configureSecurity
import io.ktor.server.application.*
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

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

    install(Koog){
        llm {
            openAI(apiKey = System.getenv("") ?: "") {
                baseUrl = "https://api.openai.com"
                timeouts { // Default values shown below
                    requestTimeout = 15.minutes
                    connectTimeout = 60.seconds
                    socketTimeout = 15.minutes
                }
            }
        }
    }

//    routing {
//        route("/ai") {
//            post("/chat") {
//                val userInput = call.receiveText()
//                // Create and run a default single‑run agent using a specific model
//                val openAIClient = OpenAILLMClient("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")
//                val executor = MultiLLMPromptExecutor(LLMProvider.OpenAI to openAIClient)
//                val prompt = "I need to travel from Rotterdam to New York. How can i travel? What clothes should i bring if I leave tomorrow"
//
//                val output = AIAgent(
//                    executor = executor,
//                    llmModel = OpenAIModels.Chat.GPT5Nano
//                ).run(prompt)
//                call.respond(output)
//            }
//        }
//    }
    configureSecurity()
    configureSerialization()
    configureHTTP()
    configureRouting()
}
