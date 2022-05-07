package com.assignment.youtubedata.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.youtubedata.R
import com.assignment.youtubedata.adapters.VideosAdapter
import com.assignment.youtubedata.adapters.VideosLoadingStateAdapter
import com.assignment.youtubedata.databinding.ActivityMainBinding
import com.assignment.youtubedata.utils.RecyclerViewItemDecoration
import com.assignment.youtubedata.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val adapter =
        VideosAdapter { id: String -> watchYoutubeVideo(id) }

    private var searchJob: Job? = null

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpAdapter()
        startSearchJob()
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

    }

    @ExperimentalPagingApi
    private fun startSearchJob() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.getYoutubeVideos()
                .collectLatest {
                    adapter.submitData(it)
                }
        }
    }

    private fun snackBarClickedYoutube(name: String) {
        val parentLayout = findViewById<View>(android.R.id.content)
        Snackbar.make(parentLayout, name, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun setUpAdapter() {
        binding.allProductRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            addItemDecoration(RecyclerViewItemDecoration())
        }
        binding.allProductRecyclerView.adapter = adapter.withLoadStateFooter(
            footer = VideosLoadingStateAdapter { retry() }
        )

        adapter.addLoadStateListener { loadState ->

            if (loadState.mediator?.refresh is LoadState.Loading) {
                if (adapter.snapshot().isEmpty()) {
                    binding.progress.isVisible = true
                }
                binding.errorTxt.isVisible = false

            } else {
                binding.progress.isVisible = false
                binding.swipeRefreshLayout.isRefreshing = false
                val error = when {
                    loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                    loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                    loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    if (adapter.snapshot().isEmpty()) {
                        binding.errorTxt.isVisible = true
                        binding.errorTxt.text = it.error.localizedMessage
                    }

                }

            }
        }

    }

    private fun watchYoutubeVideo(id: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id")
        )
        try {
            startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(webIntent)
        }
    }

    private fun retry() {
        adapter.retry()
    }


}
