package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.repository.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.response.LoginResult
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult?>()
    val loginResult: MutableLiveData<LoginResult?> = _loginResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun login(email: String, password: String) {
        repository.login(email, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!.loginResult
                    if (result != null) {
                        viewModelScope.launch {
                            repository.saveLoginResult(result)
                        }
                        _loginResult.postValue(result)
                    } else {
                        _error.postValue("Login gagal")
                    }
                } else {
                    _error.postValue("Login gagal")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _error.postValue(t.message)
            }
        })
    }
}