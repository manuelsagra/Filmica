package com.manuelsagra.filmica.data

import android.arch.paging.PageKeyedDataSource
import android.content.Context

class TrendingDataSource(
        context: Context,
        language: String
): PageKeyedDataSource<Int, Film>() {
    private val context = context
    private val language = language

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Film>) {
        FilmsRepo.trendingFilms(context, 1, language, { films, pages ->
            callback.onResult(films, null, if (pages > 1) 2 else null)
        }, { error ->
            error.printStackTrace()
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Film>) {
        FilmsRepo.trendingFilms(context, params.key, language, { films, pages ->
            callback.onResult(films, if (params.key < pages) params.key + 1 else null)
        }, { error ->
            error.printStackTrace()
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Film>) {
        FilmsRepo.trendingFilms(context, params.key, language, { films, pages ->
            callback.onResult(films, if (params.key > 1) params.key - 1 else null)
        }, { error ->
            error.printStackTrace()
        })
    }

}