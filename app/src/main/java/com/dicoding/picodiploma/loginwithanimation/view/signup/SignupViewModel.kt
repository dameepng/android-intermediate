package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.Repository.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel(private val repository: StoryRepository) : ViewModel() {

    fun register(
        name: String,
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        repository.register(name, email, password).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!
                    callback(responseBody.error == false, responseBody.message ?: "Success")
                } else {
                    callback(false, "Failed to register")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                callback(false, t.message ?: "An error occurred")
            }
        })
    }
}