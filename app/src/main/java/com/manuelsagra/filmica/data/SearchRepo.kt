package com.manuelsagra.filmica.data

import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.manuelsagra.filmica.data.ApiRoutes.discoverUrl
import com.manuelsagra.filmica.data.ApiRoutes.searchUrl
import com.manuelsagra.filmica.data.ApiRoutes.trendingUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object SearchRepo {
    fun searchFilms(
            context: Context,
            query: String,
            callbackSuccess: ((MutableList<Film>) -> Unit),
            callbackError: ((VolleyError) -> Unit)
    ) {
        requestSearchFilms(callbackSuccess, callbackError, context, query)
    }

    private fun requestSearchFilms(callbackSuccess: (MutableList<Film>) -> Unit, callbackError: (VolleyError) -> Unit, context: Context, query: String) {
        val url = searchUrl(query)
        Log.d("SEARCH URL", url)
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val newFilms = Film.parseFilms(response)
            FilmsRepo.addNew(newFilms)
            callbackSuccess(newFilms)
        }, { error ->
            callbackError(error)
        })

        Volley.newRequestQueue(context)
                .add(request)
    }
}