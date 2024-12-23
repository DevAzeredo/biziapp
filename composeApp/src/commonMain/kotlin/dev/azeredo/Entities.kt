package dev.azeredo

import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    val id: Long? = null,
    val fullName: String,
    val dateOfBirth: String,
    val gender: String? = null,
    val email: String,
    val phone: String,
    val residentialAddress: String,
    val isAvailable: Boolean,
    val lastKnownLocation: Location? = null,
    val rating: Double
)

@Serializable
data class Company(
    val id: Long? = null,
    val name: String = "",
    val description: String = "",
    val address: String = "",
    val logoUrl: String? = null
)

@Serializable
data class JobOpportunity(
    val id: Long? = null,
    val companyId: Long? = null,
    val title: String,
    val description: String,
    val category: String,
    val address: String,
    val location: Location,
    val startDateTime: String,
    val durationInHours: Int,
    val payRate: Double,
    val status: JobStatus,
    val companyName: String,
    val companyLogoUrl: String? = null
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