package dev.azeredo

import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    val id: Long,
    val fullName: String,
    val dateOfBirth: String,
    val gender: String? = null,
    val email: String,
    val phone: String,
    val residentialAddress: String,
    val isAvailable: Boolean,
    val lastKnownLocation: Location?,
    val rating: Double,
)
@Serializable
data class Company(
    val id: Long = 0L,
    val name: String = "",
    val description: String = "",
    val address: String = "",
    val logoUrl: String = ""
)
@Serializable
data class JobOpportunity(
    val id: Long,
    val title: String,
    val description: String,
    val companyName: String,
    val companyLogoUrl: String? = null,
    val category: String,
    val address: String,
    val location: Location,
    val startDateTime: String,
    val durationInHours: Int,
    val payRate: Double,
    val status: JobStatus,
)
@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double
)
@Serializable
enum class JobStatus {
    OPEN,
    PENDING,
    COMPLETED,
    CANCELLED
}
