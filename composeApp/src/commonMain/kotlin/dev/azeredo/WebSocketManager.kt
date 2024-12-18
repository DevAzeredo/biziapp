package dev.azeredo

import dev.azeredo.Constants.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow


class WebSocketManager(private val httpClient: HttpClient) {

     var session: WebSocketSession? = null

    suspend fun connect() {
        try {
            session = httpClient.webSocketSession {
                url("wss://$BASE_URL/ws")
            }
            println("Conexão WebSocket estabelecida")

        } catch (e: Exception) {
            println("Erro ao conectar ao WebSocket: ${e.message}")
        }
    }

    suspend fun sendMessage(message: String) {
        session?.send(Frame.Text(message))
    }

    suspend fun disconnect() {
        session?.close()
        println("Conexão WebSocket encerrada")
    }
}