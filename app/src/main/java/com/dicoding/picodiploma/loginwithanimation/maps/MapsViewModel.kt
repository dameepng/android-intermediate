package com.dicoding.picodiploma.loginwithanimation.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.dicoding.picodiploma.loginwithanimation.repository.StoryRepository
import kotlinx.coroutines.Dispatchers

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getStoriesWithLocation(token: String) = liveData(Dispatchers.IO) {
        val response = repository.getStoriesWithLocation(token)
        emit(response.listStory)
    }

    fun getSession() = repository.getSession().asLiveData()
}
