package com.droid.offlineStorage.retrofit

import com.droid.offlineStorage.model.ExResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by Akhtar
 */
interface RestApi {
    @GET("posts")
    suspend fun getAllPosts() : Response<List<ExResponse>>
}