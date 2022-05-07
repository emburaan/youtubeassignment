package com.assignment.youtubedata.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.assignment.youtubedata.api.YoutubeApi
import com.assignment.youtubedata.data.datasource.YoutubeDataSource
import com.assignment.youtubedata.models.ItemsItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YoutubeRepository @Inject constructor(
    private val youtubeApi: YoutubeApi
) {

    @ExperimentalPagingApi
    fun getYoutubePopularVideos(): Flow<PagingData<ItemsItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 5),
            pagingSourceFactory = { YoutubeDataSource(youtubeApi) }
        ).flow
    }

}