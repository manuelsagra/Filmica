package com.manuelsagra.filmica.view.utils

import com.manuelsagra.filmica.data.Film

interface FilmClickListener {
    fun onItemClicked(film: Film)
}
