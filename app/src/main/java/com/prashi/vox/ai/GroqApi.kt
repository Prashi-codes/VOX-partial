package com.prashi.vox.ai

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GroqApi {

    @Headers("Content-Type: application/json")
    @POST("openai/v1/chat/completions")
    fun chat(
        @Body request: GroqRequest
    ): Call<GroqResponse>
}