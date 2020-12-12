package com.example.grammarapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GrammarApi {
    @POST("get-url")
    fun createHtml(@Body post: PostHTML) : Call<PostHTML>
}