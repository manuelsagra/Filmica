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

    fun insertNew(newFilms: List<Film>) {
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
            callbackSuccess.invoke(films)
        }
    }
}