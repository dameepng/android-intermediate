package com.dicoding.picodiploma.loginwithanimation.view.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.Repository.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.response.UploadStoryResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun uploadStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        onResult: (UploadStoryResponse) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val session = storyRepository.getSession().first()
                val result = storyRepository.uploadStory(session.token, photo, description)
                onResult(result)
            } catch (e: Exception) {
                onResult(UploadStoryResponse(true, e.message ?: "Unknown error occurred"))
            }
        }
    }
}
