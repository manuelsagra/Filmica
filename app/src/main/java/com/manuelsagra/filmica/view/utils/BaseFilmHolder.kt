package com.manuelsagra.filmica.view.utils

import android.support.v7.widget.RecyclerView
import android.view.View
import com.manuelsagra.filmica.data.Film

open class BaseFilmHolder(
        itemView: View,
        clickListener: ((Film) -> Unit)? = null
): RecyclerView.ViewHolder(itemView) {
    lateinit var film: Film

    init {
        itemView.setOnClickListener {
            clickListener?.invoke(this.film)
        }
    }

    open fun bindFilm(film: Film) {
        this.film = film
    }
}