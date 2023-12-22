package com.project.greenbean.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.project.greenbean.data.pref.UserPreference
import com.project.greenbean.data.response.LoginResponse
import com.project.greenbean.data.response.PostingResponse
import com.project.greenbean.data.response.PredictResponse
import com.project.greenbean.data.response.Prediction
import com.project.greenbean.data.response.RegisterResponse
import com.project.greenbean.data.response.UserResponse
import com.project.greenbean.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class Repository private constructor(
    private val api : ApiService,
    private val pref: UserPreference,
    private val apiServiceWithoutHeader: ApiService
) {

    fun login(email: String, password: String) : LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiServiceWithoutHeader.login(email, password)
            if (response.code != "400") {
                val token = response.token
                if (token != null) {
                    emit(Result.Success(token))
                } else {
                    emit(Result.Error(response.message!!))
                }
            } else {
                emit(Result.Error(response.message!!))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error("Login failed: $errorMessage"))
        } catch (e: Exception) {
            emit(Result.Error("Internet Issues"))
        }
    }

    fun register(email: String, password: String, confirmPassword: String) : LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiServiceWithoutHeader.register(email, password, confirmPassword)
            if (response.code != "400") {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message!!))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error("Register failed: $errorMessage"))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveSession(token: String) {
        pref.saveSession(token)
    }

    fun getSession(): Flow<String> {
        return pref.getSession()
    }

    suspend fun logout() {
        pref.logout()
    }

    fun getUser(): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = api.getUser()
            if (response.code != "400") {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message!!))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error("Login failed: $errorMessage"))
        } catch (e: Exception) {
            emit(Result.Error("Internet Issues"))
        }
    }

    fun getAllPosting(): LiveData<Result<PostingResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = api.getPosting()
            if (!(response.code == "400" || response.code == "500")) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message!!))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error("Login failed: $errorMessage"))
        } catch (e: Exception) {
            emit(Result.Error("Internet Issues"))
        }
    }

    fun uploadPosting(image: MultipartBody.Part, caption: RequestBody): LiveData<Result<PostingResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = api.uploadPosting(image, caption)
            if (!(response.code == "400" || response.code == "500")) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message!!))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error("Upload failed: $errorMessage"))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updateProfile(
        name: String,
        email: String,
        address: String,
        phone: String,
    ) : LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = api.updateProfile(name, email, address, phone)
            if (!(response.code == "400" || response.code == "500")) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message!!))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error("Upload failed: $errorMessage"))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun predictImage(image: File): LiveData<Result<PredictResponse>> = liveData {
        emit(Result.Loading)
        try {
            val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                image.name,
                requestImageFile
            )
            val response = api.predictImage(imageMultipart)

            if (response.isSuccessful) {
                val data = response.body()
                data?.let {
                    emit(Result.Success(it))
                }
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, PredictResponse::class.java)
            val errorMessage = errorBody.message ?: "Prediction failed"
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Internet Issues"))
        }
    }



    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            apiServiceWithoutHeader: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreference, apiServiceWithoutHeader)
            }.also { instance = it }
    }
}