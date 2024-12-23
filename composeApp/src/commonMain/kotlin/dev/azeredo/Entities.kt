package dev.azeredo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    val id: Long? = null,
    @SerialName("full_name") val fullName: String,
    @SerialName("date_of_birth") val dateOfBirth: String,
    val gender: String? = null,
    val email: String,
    val phone: String,
    @SerialName("residential_address") val residentialAddress: String,
    @SerialName("is_available") val isAvailable: Boolean,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val rating: Double
)

@Serializable
data class Company(
    val id: Long? = null,
    val name: String = "",
    val description: String = "",
    val address: String = "",
    @SerialName("logo_url") val logoUrl: String? = null
)

@Serializable
data class JobOpportunity(
    val id: Long? = null,
    @SerialName("company_id") val companyId: Long? = null,
    val title: String,
    val description: String,
    val category: String,
    val address: String,
    val location: Location,
    @SerialName("start_date_time") val startDateTime: String,
    @SerialName("duration_in_hours")  val durationInHours: Int,
    @SerialName("pay_rate")  val payRate: Double,
    val status: JobStatus,
    @SerialName("company_name") val companyName: String,
    @SerialName("company_logo_url") val companyLogoUrl: String? = null
)

@Serializable
data class Location(
    val latitude: Double, val longitude: Double
)

@Serializable
data class NewUser(
    val login: String, val password: String
)

@Serializable
enum class JobStatus {
    OPEN, PENDING, COMPLETED, CANCELLED
}