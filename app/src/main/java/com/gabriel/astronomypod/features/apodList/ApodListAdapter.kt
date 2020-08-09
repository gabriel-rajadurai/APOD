package com.gabriel.astronomypod.features.apodList

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.gabriel.astronomypod.R
import com.gabriel.data.models.APOD
import kotlinx.android.synthetic.main.item_apod.view.*

class ApodListAdapter : ListAdapter<APOD, ApodListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_apod, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(apod: APOD) {
            Glide.with(itemView)
                .load(Uri.parse(apod.url))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.ivApod)
        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<APOD>() {
            override fun areItemsTheSame(oldItem: APOD, newItem: APOD): Boolean {
                return oldItem.url == newItem.url // Add
            }

            override fun areContentsTheSame(oldItem: APOD, newItem: APOD): Boolean {
                return oldItem == newItem
            }
        }
    }
}