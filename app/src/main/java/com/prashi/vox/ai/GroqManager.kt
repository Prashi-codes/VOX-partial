package com.prashi.vox.ai

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GroqManager {

    private val apiKey = "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"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->

            val request: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $apiKey")
                .build()

            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.groq.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(GroqApi::class.java)

    fun askAI(
        question: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {

        val request = GroqRequest(
            model = "llama-3.3-70b-versatile",
            messages = listOf(

                Message(
                    "system",
                    "You are VOX, a smart voice assistant. Reply in plain text only. Never use markdown, headings, stars, hashtags, bullet points, code blocks, tables, or symbols like === or ---. Keep answers short, natural and suitable for text to speech. Limit answers to about 100 words unless the user asks for more details."
                ),

                Message(
                    "user",
                    question
                )
            )
        )

        api.chat(request).enqueue(object : Callback<GroqResponse> {

            override fun onResponse(
                call: Call<GroqResponse>,
                response: Response<GroqResponse>
            ) {

                if (response.isSuccessful) {

                    var answer =
                        response.body()
                            ?.choices
                            ?.firstOrNull()
                            ?.message
                            ?.content
                            ?: "No response"

                    answer = answer
                        .replace("**", "")
                        .replace("*", "")
                        .replace("#", "")
                        .replace("```", "")
                        .replace("---", "")
                        .replace("===", "")
                        .replace(Regex("\\n{2,}"), "\n")
                        .trim()

                    onSuccess(answer)

                } else {

                    onError("Error ${response.code()}")
                }
            }

            override fun onFailure(
                call: Call<GroqResponse>,
                t: Throwable
            ) {
                onError(t.message ?: "Unknown error")
            }
        })
    }
}