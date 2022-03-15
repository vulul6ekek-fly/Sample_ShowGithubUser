package com.fly.sample_showgithubuser.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Accept: application/vnd.github.v3.full+json")
    @GET("users")
    fun getUsers( @Query("since")  since: Int,@Query("per_page")  pageNumber: Int): Call<ResponseBody>

    @Headers("Accept: application/vnd.github.v3.full+json")
    @GET("users/{login}")
    fun getOneUser(@Path("login") login: String): Call<ResponseBody>
}