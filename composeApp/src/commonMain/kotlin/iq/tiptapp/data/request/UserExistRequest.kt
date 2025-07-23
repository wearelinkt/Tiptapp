package iq.tiptapp.data.request

import kotlinx.serialization.Serializable

@Serializable
data class UserExistRequest(
    val id: String
)