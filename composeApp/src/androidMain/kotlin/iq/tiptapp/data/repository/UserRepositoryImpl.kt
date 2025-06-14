package iq.tiptapp.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import iq.tiptapp.data.request.RegisterRequest
import iq.tiptapp.data.request.UserExistRequest
import iq.tiptapp.domain.repository.UserRepository
import iq.tiptapp.utils.RegisterUserException
import iq.tiptapp.utils.UserExistException

class UserRepositoryImpl(
    private val httpClient: HttpClient,
) : UserRepository {

    override suspend fun registerUser(phoneNumber: String, userId: String): Result<Unit> {
        val requestBody = RegisterRequest(
            id = userId,
            phoneNumber = phoneNumber
        )
        try {
            val response = httpClient.post("users") {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            if(response.status.value == HttpStatusCode.Created.value) {
                return Result.success(Unit)
            }
            return Result.failure(RegisterUserException())
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun userExist(userId: String): Result<Unit> {
        val requestBody = UserExistRequest(id = userId)
        try {
            val response = httpClient.post("users/exists") {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            if(response.status.value == HttpStatusCode.OK.value) {
                return Result.success(Unit)
            }
            return Result.failure(UserExistException())
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}