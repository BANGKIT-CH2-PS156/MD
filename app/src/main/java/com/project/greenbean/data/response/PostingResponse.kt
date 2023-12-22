package com.project.greenbean.data.response

import com.google.gson.annotations.SerializedName

data class PostingResponse(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("data")
	val posting: List<Posting?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class createPosting(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Posting(

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("id_posting")
	val idPosting: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("caption")
	val caption: String? = null,

	@field:SerializedName("id_user")
	val idUser: Int? = null,

	@field:SerializedName("create_at")
	val createAt: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)
