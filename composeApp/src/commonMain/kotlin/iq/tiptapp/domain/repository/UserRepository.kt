package iq.tiptapp.domain.repository

interface UserRepository {

    suspend fun registerUser(phoneNumber: String, userId: String) : Result<Unit>

    suspend fun userExist(userId: String) : Result<Unit>
}