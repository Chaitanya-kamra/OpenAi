package com.chaitanya.openaiassignment.ui.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.util.Util
import com.chaitanya.openaiassignment.R
import com.chaitanya.openaiassignment.databinding.ItemLinkBinding
import com.chaitanya.openaiassignment.domain.model.Link
import com.chaitanya.openaiassignment.utils.Utils
import com.chaitanya.openaiassignment.utils.Utils.loadImage


class DashboardLinksAdapter(private val clickListener: (String) -> Unit
) :ListAdapter<Link, DashboardLinksAdapter.ViewHolder>(DiffUtilCallBack()) {

    inner class ViewHolder(binding: ItemLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvTitle = binding.tvTitle
        val tvDate = binding.tvDate
        val clickCount = binding.tvClickText
        val tvLink = binding.tvLink
        val icon = binding.ivIcon
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLinkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.apply {
            tvTitle.text = item.title
            tvDate.text = Utils.formatDate(item.created_at)
            tvLink.text = item.web_link
            clickCount.text = item.total_clicks.toString()
            icon.loadImage(item.original_image, R.drawable.placeholder)
            tvLink.setOnClickListener { clickListener(item.web_link) }
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Link>() {

        override fun areItemsTheSame(
            oldItem: Link,
            newItem: Link
        ): Boolean {
            return oldItem.url_id== newItem.url_id && oldItem.smart_link == newItem.smart_link
        }

        override fun areContentsTheSame(
            oldItem: Link,
            newItem: Link
        ): Boolean {
            return oldItem == newItem
        }

    }


}