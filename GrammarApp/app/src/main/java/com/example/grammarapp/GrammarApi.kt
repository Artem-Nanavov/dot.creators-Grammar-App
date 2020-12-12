package com.example.grammarapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GrammarApi {
    @POST("get-text")
    fun createHtml(@Body post: PostText) : Call<PostText>

    @GET("get")
    fun getTest() : Call<Test>
}