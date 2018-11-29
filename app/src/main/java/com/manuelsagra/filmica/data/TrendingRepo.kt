package com.manuelsagra.filmica.data

import android.arch.persistence.room.Room
import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.manuelsagra.filmica.data.ApiRoutes.discoverUrl
import com.manuelsagra.filmica.data.ApiRoutes.trendingUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object TrendingRepo {
    private val films: MutableList<Film> = mutableListOf()

    fun trendingFilms(
            context: Context,
            callbackSuccess: ((MutableList<Film>) -> Unit),
            callbackError: ((VolleyError) -> Unit)
    ) {
        if (films.isEmpty()) {
            requestTrendingFilms(callbackSuccess, callbackError, context)
        } else {
            callbackSuccess(films)
        }
    }

    private fun requestTrendingFilms(callbackSuccess: (MutableList<Film>) -> Unit, callbackError: (VolleyError) -> Unit, context: Context) {
        val url = trendingUrl()
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val newFilms = Film.parseFilms(response)
            films.addAll(newFilms)
            FilmsRepo.insertNew(newFilms)
            callbackSuccess(films)
        }, { error ->
            callbackError(error)
        })

        Volley.newRequestQueue(context)
                .add(request)
    }
}