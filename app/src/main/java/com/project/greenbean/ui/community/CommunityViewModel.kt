package com.project.greenbean.ui.community

import androidx.lifecycle.ViewModel
import com.project.greenbean.data.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CommunityViewModel(private val repository: Repository): ViewModel() {

    fun getAllPosting() = repository.getAllPosting()

    fun uploadPosting(
        image: MultipartBody.Part,
        caption: RequestBody,
    ) = repository.uploadPosting(image, caption)
}