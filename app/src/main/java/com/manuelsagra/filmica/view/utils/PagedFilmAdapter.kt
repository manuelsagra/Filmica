package com.manuelsagra.filmica.view.utils

import android.arch.paging.PagedListAdapter
import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.manuelsagra.filmica.data.Film

open class PagedFilmAdapter<VH: BaseFilmHolder>(
        @LayoutRes val layoutItem: Int,
        val holderCreator: ((View) -> VH)
): PagedListAdapter<Film, VH>(
        filmDiffCallback
) {
    companion object {
        val filmDiffCallback = object : DiffUtil.ItemCallback<Film>() {
            override fun areItemsTheSame(oldItem: Film?, newItem: Film?): Boolean {
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItem: Film?, newItem: Film?): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(recyclerView: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(recyclerView.context).inflate(layoutItem, recyclerView, false)

        return holderCreator(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val film = getItem(position)
        film?.let {
            holder.bindFilm(it)
        }
    }

    fun clear() {
        currentList?.dataSource?.invalidate()
    }
}