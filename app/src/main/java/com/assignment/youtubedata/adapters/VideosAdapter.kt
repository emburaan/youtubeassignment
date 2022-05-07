package com.assignment.youtubedata.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.assignment.youtubedata.databinding.AdapterItemBinding
import com.assignment.youtubedata.models.ItemsItem

class VideosAdapter(private val clicked: (String) -> Unit) :
    PagingDataAdapter<ItemsItem, VideosAdapter.VideosViewHolder>(
        VideosDiffCallback()
    ) {


    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {

        return VideosViewHolder(
            AdapterItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    inner class VideosViewHolder(
        private val binding: AdapterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ItemsItem?) {
            binding.let {
                it.root.setOnClickListener {
                    data?.id?.let { it1 -> clicked.invoke(it1) }
                }
                it.name.text = data?.snippet?.title
                Glide.with(it.ivThumbnail.context).load(data?.snippet?.thumbnails?.high?.url)
                    .into(it.ivThumbnail)
            }

        }
    }

    private class VideosDiffCallback : DiffUtil.ItemCallback<ItemsItem>() {
        override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
            return oldItem == newItem
        }
    }

}