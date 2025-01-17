package dev.azeredo.repositories

import dev.azeredo.Constants.BASE_URL
import dev.azeredo.NewUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.http.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import io.ktor.client.request.setBody
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class AuthRepository(private val httpClient: HttpClient) {
    suspend fun login(signInData: NewUser): String {
        return withContext(Dispatchers.IO) {
            val resp = httpClient.post("http://$BASE_URL/api/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(signInData)
            }
            if (resp.status.isSuccess()) {
                resp.body()
            } else {
                throw Exception("Erro ao fazer login")
            }
        }
    }

    suspend fun register(newUser: NewUser): String = withContext(Dispatchers.IO) {
        val resp = httpClient.post("http://$BASE_URL/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(newUser)
        }
        if (resp.status.isSuccess()) {
            resp.body()
        } else {
            throw Exception("Erro ao registrar")
        }
    }
}
