package com.assignment.youtubedata.api

import com.assignment.youtubedata.models.YoutubePopularVideoApiResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface YoutubeApi {

    @GET("/youtube/v3/videos")
    suspend fun getYoutubePopularVideos(@QueryMap query: Map<String, String>): YoutubePopularVideoApiResponse

}