package com.project.greenbean.data.retrofit

import com.project.greenbean.data.response.LoginResponse
import com.project.greenbean.data.response.PostingResponse
import com.project.greenbean.data.response.PredictResponse
import com.project.greenbean.data.response.RegisterResponse
import com.project.greenbean.data.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String,
    ) : RegisterResponse


    @GET("users/one")
    suspend fun getUser() : UserResponse

    @GET("posting")
    suspend fun getPosting() : PostingResponse

    @Multipart
    @POST("posting")
    suspend fun uploadPosting(
        @Part image: MultipartBody.Part,
        @Part("caption") caption: RequestBody,
    ) : PostingResponse

    @FormUrlEncoded
    @PATCH("users")
    suspend fun updateProfile(
        @Field("name") name: String,
        @Field("job") email: String,
        @Field("address") address: String,
        @Field("phone") phone: String,
    ) : UserResponse

    @Multipart
    @POST("predict")
    suspend fun predictImage(
        @Part img: MultipartBody.Part,
    ): Response<PredictResponse>

}