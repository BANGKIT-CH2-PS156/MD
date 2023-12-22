package com.project.greenbean.ui.verifikasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.greenbean.data.Repository
import com.project.greenbean.data.Result
import com.project.greenbean.data.response.PredictResponse
import com.project.greenbean.data.response.Prediction
import java.io.File

class VerifikasiViewModel(private val repository: Repository) : ViewModel() {

    fun predictImage(image: File?): LiveData<Result<PredictResponse>> {
        image?.let {
            return repository.predictImage(it)
        }
        return MutableLiveData<Result<PredictResponse>>().apply {
            value = Result.Error("Invalid prediction data")
        }
    }


    companion object {
        fun create(repository: Repository): VerifikasiViewModel {
            return VerifikasiViewModel(repository)
        }
    }
}
