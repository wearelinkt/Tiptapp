package iq.tiptapp.domain.repository

interface UserRepository {

    suspend fun registerUser(phoneNumber: String, userId: String) : Result<Boolean>

    suspend fun userExist(userId: String) : Result<Boolean>
}