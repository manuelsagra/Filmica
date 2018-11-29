package com.manuelsagra.filmica.view.utils

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.manuelsagra.filmica.data.Film

open class BaseFilmAdapter<VH: BaseFilmHolder>(
        @LayoutRes val layoutItem: Int,
        val holderCreator: ((View) -> VH)
): RecyclerView.Adapter<VH>() {

    protected var list = mutableListOf<Film>()

    override fun onCreateViewHolder(recyclerView: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(recyclerView.context).inflate(layoutItem, recyclerView, false)

        return holderCreator(itemView)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindFilm(list[position])
    }

    fun setFilms(films: MutableList<Film>) {
        list.clear()
        list.addAll(films)
        notifyDataSetChanged()
    }

    fun getFilm(position: Int): Film {
        return list.get(position)
    }

    fun removeItemAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }
}