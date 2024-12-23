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

class AuthRepository(private val httpClient: HttpClient) {
    suspend fun login(signInData: NewUser): String  {
        return withContext(Dispatchers.IO) {
            httpClient.post("https://$BASE_URL/login") {
                contentType(ContentType.Application.Json)
                setBody(signInData)
            }.body()
        }
    }

    suspend fun register(newUser: NewUser): String {
        return withContext(Dispatchers.IO) {
            httpClient.post("https://$BASE_URL/register") {
                contentType(ContentType.Application.Json)
                setBody(newUser)
            }.body()
        }
    }
}
