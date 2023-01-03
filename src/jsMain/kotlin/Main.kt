import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

fun main() {
    val client = HttpClient(Js) {}
    var i by mutableStateOf("")

    renderComposable(rootElementId = "root") {
        Div({
            style {
                height(100.vh)
                margin(0.px)
                width(100.vw)
            }
        }) {
            Input(type = InputType.Url) {
                onInput {
                    val input = it.value.trim()

                    if (input.startsWith("http")) {
                        GlobalScope.launch {
                            i = client.shorterCall(input)
                        }
                    } else {
                        i = "NaN"
                    }
                }
            }
            Text(i)
        }
    }
}

suspend fun HttpClient.shorterCall(url: String): String {
    val response = get(url) {
        contentType(ContentType.Application.Json)
    }
    return response.bodyAsText()
}

