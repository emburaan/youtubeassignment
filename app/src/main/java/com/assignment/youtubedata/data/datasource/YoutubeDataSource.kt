package com.assignment.youtubedata.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.assignment.youtubedata.api.YoutubeApi
import com.assignment.youtubedata.models.ItemsItem
import retrofit2.HttpException
import java.io.IOException

class YoutubeDataSource(private val youtubeApi: YoutubeApi) :
    PagingSource<String, ItemsItem>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ItemsItem> {
        val pageToken = params.key ?: ""
        return try {
            val response = youtubeApi.getYoutubePopularVideos(generateQueryDataForPage(pageToken))
            val videoResponse = response.items
            LoadResult.Page(
                data = videoResponse,
                prevKey = response.prevPageToken,
                nextKey = response.nextPageToken
            )

        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection")
            LoadResult.Error(error)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<String, ItemsItem>): String {
        return ""
    }

    private fun generateQueryDataForPage(pageToken: String): HashMap<String, String> {
        val query = HashMap<String, String>()
        query["chart"] = "mostPopular"
        query["part"] = "snippet,contentDetails,statistics"
        query["regionCode"] = "US"
        query["key"] = "AIzaSyCCGFgAb-4aRy1csf0RcYB4-aP9pJDqFew"
        query["maxResults"] = "10"
        query["pageToken"] = pageToken
        return query
    }
}