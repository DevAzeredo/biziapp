package dev.azeredo.repositories

import dev.azeredo.Company
import dev.azeredo.Constants.BASE_URL
import dev.azeredo.api.AuthManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class CompanyRepository(private val httpClient: HttpClient) {
    suspend fun createCompany(company: Company): Company {
        return withContext(Dispatchers.IO) {
            httpClient.post("https://$BASE_URL/companies") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                    append(HttpHeaders.Authorization, "Bearer ${AuthManager.getToken()}")
                }
                contentType(ContentType.Application.Json)
                setBody(company)
            }.body()
        }
    }
}
