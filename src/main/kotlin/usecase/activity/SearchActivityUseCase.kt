package com.example.usecase.activity

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.Tool
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.reflect.asTools
import ai.koog.agents.ext.agent.reActStrategy
import com.example.core.ObjectResult
import com.example.usecase.BaseInputUseCase
import model.Activity
import ai.koog.ktor.aiAgent
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.clients.openai.OpenAILLMClient
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.executor.llms.MultiLLMPromptExecutor
import ai.koog.prompt.llm.LLMProvider
import com.example.core.FilterActivitiesTool
import com.example.repository.IActivityRepository
import dtos.activity.ActivityIdResult
import kotlinx.serialization.json.Json
import repository.ActivityRepository

class SearchActivityUseCase(
    val filterActivitiesUseCase: FilterActivitiesUseCase,
    val repo: IActivityRepository
) : BaseInputUseCase<String, List<Activity>> {
    override suspend fun execute(input: String): ObjectResult<List<Activity>> {
        // Create and run a default single‑run agent using a specific model
        val apiKey = System.getenv("OPENAI_API_KEY")
        val openAIClient = OpenAILLMClient(apiKey)
        val executor = MultiLLMPromptExecutor(LLMProvider.OpenAI to openAIClient)

        // Define available tools
        val tools = ToolRegistry {
            FilterActivitiesTool(filterActivitiesUseCase, repo).asTools().forEach { tool ->
                tool(tool as Tool<*, *>)
            }
        }

        val agent = AIAgent(
            executor = executor,
            llmModel = OpenAIModels.Chat.GPT4o,
            toolRegistry = tools,
            systemPrompt =
            """
            Je bent een AI-assistent die helpt bij het vinden van de best passende activiteiten.
            Je krijgt een gebruikersvraag in het Nederlands (bijv. "Ik wil iets sportiefs doen morgen in Amsterdam").
            
            Je taak:
            1. Begrijp de intentie van de gebruiker zo volledig mogelijk.
            2. Interpreteer de vraag meerdere keren om erachter te komen welke activiteiten echt passen.
               - Kijk naar type activiteit, prijs, locatie, datum, moeilijkheidsgraad, en andere relevante eigenschappen.
               - Als een specifieke filter te streng is, probeer een bredere interpretatie van de vraag.
            3. Gebruik de beschikbare tools om de gevonden filters te testen en activiteiten te zoeken.
            4. Geef maximaal 3 resultaten terug, maar **zorg dat er altijd minimaal 1 resultaat is**, tenzij er echt niets bestaat.
            5. Sorteer resultaten op relevantie: de activiteit die het beste past op basis van de gebruikersvraag komt eerst.
            6. Geef alleen JSON terug in exact dit formaat:

            {
              "activityIds": [1, 2, 3]
            }
            
            - Geef GEEN volledige activiteiten terug
            - Geef GEEN extra tekst
            - Alleen geldige IDs die bestaan
            """.trimIndent()
        )

        val output = agent.run(input)

        val activityIds = parseActivityIdsFromJson(output)

        if (activityIds.isEmpty()) {
            return ObjectResult.success(emptyList())
        }

        val activities = repo.getByQuery {
            it.id in activityIds
        }

        // Sorteer in volgorde van AI-relevantie
        val sorted = activityIds.mapNotNull { id ->
            activities.find { it.id == id }
        }

        return ObjectResult.success(sorted)
    }

    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        coerceInputValues = true
    }
    private fun parseActivityIdsFromJson(json: String): List<Int> {

        // 1️⃣ Eerst: correcte JSON proberen
        try {
            val result = jsonParser.decodeFromString<ActivityIdResult>(json)
            if (result.activityIds.isNotEmpty()) {
                return result.activityIds
            }
        } catch (_: Exception) {
            // bewust leeg – we vallen terug op regex
        }

        // 2️⃣ Fallback: alle integers uit de string halen
        return try {
            val regex = Regex("""\b\d+\b""")
            regex.findAll(json)
                .map { it.value.toInt() }
                .distinct()
                .toList()
        } catch (e: Exception) {
            println("Failed to extract activity IDs via regex: ${e.message}")
            emptyList()
        }
    }
}