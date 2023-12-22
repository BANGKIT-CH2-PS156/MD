package com.project.greenbean.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("verify")
	val verify: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("job")
	val job: String? = null,

	@field:SerializedName("create_at")
	val createAt: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)
