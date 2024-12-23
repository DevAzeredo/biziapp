package dev.azeredo.repositories
import dev.azeredo.Constants.BASE_URL
import dev.azeredo.JobOpportunity
import dev.azeredo.api.AuthManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class JobOpportunityRepository(private val httpClient: HttpClient) {
    suspend fun createJobOpportunity(jobOpportunity: JobOpportunity): JobOpportunity {
        return withContext(Dispatchers.IO) {
            httpClient.post("$BASE_URL/jobs") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                    append(HttpHeaders.Authorization, "Bearer ${AuthManager.getToken()}")
                }
                setBody(jobOpportunity)
            }.body()
        }
    }
}