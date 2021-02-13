import components.TodoList
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.pingPeriod
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.timeout
import io.ktor.response.respondTextWriter
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.html.h2
import mu.KotlinLogging
import net.justmachinery.shade.AddScriptStrategy
import net.justmachinery.shade.ShadeRoot
import java.time.Duration

val root = ShadeRoot(
    endpoint = "/ws",
    addScriptStrategy = AddScriptStrategy.Inline(false)
)

fun main() {

    val logger = KotlinLogging.logger {}

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(5)
            timeout = Duration.ofSeconds(30)
        }

        routing {
            webSocket("/ws") {
                val handler = root.handler(
                    send = { this.outgoing.sendBlocking(Frame.Text(it)) },
                    disconnect = { }
                )
                try {
                    this.incoming.consumeEach {
                        (it as? Frame.Text)?.readText()?.let {
                            try {
                                handler.onMessage(it)
                            } catch (e: Throwable) {
                                logger.warn(e) { "Failed on message: $it" }
                            }
                        }
                    }
                } finally {
                    handler.onDisconnect()
                }
            }

            get("/") {
                call.respondTextWriter(ContentType.Text.Html, HttpStatusCode.OK) {
                    root.render(this) {
                        head {}
                        body { tag ->
                            //Adding this line is convenient because Kotlin does not yet allow multiple receivers on a lambda
                            tag.run {
                                h2 {
                                    +"Shade test page"
                                }
                                //Look at TodoList for a more in-depth overview of how to render with shade
                                add(TodoList.Props("Foo"))
                            }
                        }
                    }

                }
            }
        }
    }.start(wait = true)

}