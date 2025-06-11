package iq.tiptapp.data.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val id: String,
    val phoneNumber: String
)