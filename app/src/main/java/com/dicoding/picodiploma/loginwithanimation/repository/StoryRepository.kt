package com.dicoding.picodiploma.loginwithanimation.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dicoding.picodiploma.loginwithanimation.api.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.StoryPagingSource
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.response.LoginResult
import com.dicoding.picodiploma.loginwithanimation.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.response.StoryResponse
import com.dicoding.picodiploma.loginwithanimation.response.UploadStoryResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.awaitResponse

class StoryRepository private constructor(
    private val userPreference: UserPreference, private val apiService: ApiService
) {

    fun register(name: String, email: String, password: String): Call<RegisterResponse> {
        return apiService.register(name, email, password)
    }

    fun login(email: String, password: String): Call<LoginResponse> {
        return apiService.login(email, password)
    }

    fun getStory(): Flow<PagingData<ListStoryItem>> = flow {
        val token = runBlocking { userPreference.getToken().first() }
        emitAll(Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { StoryPagingSource(apiService, token) }
        ).flow)
    }

    suspend fun getStoriesWithLocation(token: String): StoryResponse {
        return apiService.getStoriesWithLocation("Bearer $token", 1)
    }

    fun getSession(): Flow<LoginResult> {
        return userPreference.getSession()
    }

    suspend fun saveLoginResult(loginResult: LoginResult) {
        userPreference.login(loginResult)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun uploadStory(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody
    ): UploadStoryResponse {
        val response = apiService.uploadImage("Bearer $token", photo, description).awaitResponse()
        return if (response.isSuccessful) {
            response.body() ?: UploadStoryResponse(true, "Upload failed")
        } else {
            UploadStoryResponse(true, "Upload failed: ${response.message()}")
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            pref: UserPreference, apiService: ApiService
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(pref, apiService)
        }.also { instance = it }
    }
}
