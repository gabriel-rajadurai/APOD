package com.gabriel.astronomypod.features.apodList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.ScaleType
import com.gabriel.astronomypod.common.gone
import com.gabriel.astronomypod.common.loadUrl
import com.gabriel.astronomypod.common.visible
import com.gabriel.astronomypod.databinding.ItemApodBinding
import com.gabriel.data.models.APOD
import kotlinx.android.synthetic.main.item_apod.view.*

class ApodListAdapter(private val listener: ApodItemListener) :
    ListAdapter<APOD, ApodListAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var previousExpandedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemApodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    interface ApodItemListener {
        fun shareApod(apod: APOD)
        fun downloadApod(apod: APOD)
        fun viewApod(apod: APOD)
    }

    inner class ViewHolder(private val binding: ItemApodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(apod: APOD) {

            binding.apod = apod

            if (previousExpandedItem == adapterPosition)
                itemView.infoLayout.visible()
            else
                itemView.infoLayout.gone()

            if (apod.mediaType == APOD.MEDIA_TYPE_IMAGE) {
                itemView.ivApod.loadUrl(apod.url, ScaleType.CENTER_CROP)
                itemView.ivDownload.visible()
            } else {
                itemView.ivApod.setImageResource(R.drawable.ic_play)
                itemView.ivDownload.gone()
            }

            itemView.tvTitle.setOnClickListener {
                if (previousExpandedItem == adapterPosition) {
                    itemView.infoLayout.gone()
                    previousExpandedItem = -1
                    return@setOnClickListener
                }

                if (previousExpandedItem >= 0)
                    notifyItemChanged(previousExpandedItem)

                previousExpandedItem = adapterPosition
                itemView.infoLayout.visible()
            }

            itemView.ivDownload.setOnClickListener { listener.downloadApod(apod) }
            itemView.ivShare.setOnClickListener { listener.shareApod(apod) }
            itemView.ivOpen.setOnClickListener { listener.shareApod(apod) }
            itemView.ivApod.setOnClickListener { listener.viewApod(apod) }
        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<APOD>() {
            override fun areItemsTheSame(oldItem: APOD, newItem: APOD): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: APOD, newItem: APOD): Boolean {
                return oldItem == newItem
            }
        }
    }
}