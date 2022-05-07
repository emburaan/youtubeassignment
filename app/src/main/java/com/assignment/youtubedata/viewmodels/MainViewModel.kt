package com.assignment.youtubedata.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import com.assignment.youtubedata.data.repository.YoutubeRepository
import com.assignment.youtubedata.models.ItemsItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: YoutubeRepository
) : ViewModel() {

    private var currentResult: Flow<PagingData<ItemsItem>>? = null

    @ExperimentalPagingApi
    fun getYoutubeVideos(): Flow<PagingData<ItemsItem>> {
        val newResult: Flow<PagingData<ItemsItem>> =
            repository.getYoutubePopularVideos().cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }

}