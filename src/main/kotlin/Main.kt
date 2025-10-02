import io.ktor.http.ContentType
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/") {
                call.respondText("Hello World!")
            }
            get("/todo") {
                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = """
                <h5>Hier kunnen we taken plaatsen die gedaan moeten worden</h5>
                <h3>TODO list:</h3>
                <ol>
                    <li>Request controllers maken</li>
                    <li>Use case base-class/interface maken</li>
                    <li>Repository interface maken</li>
                    <li>Repository implementatie maken voor of inMemory-db of Sql-server</li>
                </ol>
                """.trimIndent()
                )
            }
            get("/check") {
                call.respondText("Check OK!")
            }
            get("/remco") {
                call.respondText("Hallo!")
            }
        }
    }.start(wait = true)
}
