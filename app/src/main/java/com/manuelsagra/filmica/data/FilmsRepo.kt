package com.manuelsagra.filmica.data

import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
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

object FilmsRepo {
    private val films: MutableList<Film> = mutableListOf()

    @Volatile
    private var db: AppDatabase? = null

    private fun getDBInstance(context: Context): AppDatabase {
        if (db == null) {
            db = Room.databaseBuilder(context, AppDatabase::class.java, "filmica-db").build()
        }
        return db as AppDatabase
    }

    fun findFilmById(id: String): Film? {
         return films.find { film ->
             film.id == id
         }
    }

    fun addNew(newFilms: List<Film>) {
        newFilms.map {film ->
            if (!films.contains(film)) {
                films.add(film)
            }
        }
    }

    fun saveFilm(context: Context, film: Film, callbackSuccess: (Film) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            var async = async(Dispatchers.IO) {
                getDBInstance(context).filmDao().insertFilm(film)
            }
            async.await()
            callbackSuccess.invoke(film)
        }
    }

    fun deleteFilm(context: Context, film: Film, callbackSuccess: (Film) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            var async = async(Dispatchers.IO) {
                getDBInstance(context).filmDao().deleteFilm(film)
            }
            async.await()
            callbackSuccess.invoke(film)
        }
    }

    fun watchlist(context: Context, callbackSuccess: (List<Film>) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            var async = async(Dispatchers.IO) {
                getDBInstance(context).filmDao().getFilms()
            }
            var films: List<Film> = async.await()
            addNew(films)
            callbackSuccess.invoke(films)
        }
    }

    // Discover
    fun discoverFilms(
            context: Context,
            page: Int,
            callbackSuccess: ((MutableList<Film>, Int) -> Unit),
            callbackError: ((VolleyError) -> Unit)
    ) {
        requestDiscoverFilms(callbackSuccess, callbackError, context, page)
    }

    private fun requestDiscoverFilms(callbackSuccess: (MutableList<Film>, Int) -> Unit, callbackError: (VolleyError) -> Unit, context: Context, page: Int) {
        val url = discoverUrl(page)
        Log.i("DiscoverRepo", url.toString())
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val newFilms = Film.parseFilms(response)
            addNew(newFilms)
            callbackSuccess(newFilms, response.getInt("total_pages"))
        }, { error ->
            callbackError(error)
        })

        Volley.newRequestQueue(context)
                .add(request)
    }

    // Trending
    fun trendingFilms(
            context: Context,
            page: Int,
            callbackSuccess: ((MutableList<Film>, Int) -> Unit),
            callbackError: ((VolleyError) -> Unit)
    ) {
        requestTrendingFilms(callbackSuccess, callbackError, context, page)
    }

    private fun requestTrendingFilms(callbackSuccess: (MutableList<Film>, Int) -> Unit, callbackError: (VolleyError) -> Unit, context: Context, page: Int) {
        val url = trendingUrl(page)
        Log.i("TrendingRepo", url.toString())
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val newFilms = Film.parseFilms(response)
            addNew(newFilms)
            callbackSuccess(newFilms, response.getInt("total_pages"))
        }, { error ->
            callbackError(error)
        })

        Volley.newRequestQueue(context)
                .add(request)
    }
}