package com.manuelsagra.filmica.data

import android.net.Uri
import com.manuelsagra.filmica.BuildConfig

object ApiRoutes {
    fun discoverUrl(
            page: Int = 1,
            language: String = "en-US",
            sortBy: String = "popularity.desc"
    ): String {
        return getUriBuilder()
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("include_adult", "false")
                .appendQueryParameter("include_video", "false")
                .appendQueryParameter("language", language)
                .appendQueryParameter("sort_by", sortBy)
                .appendQueryParameter("page", page.toString())
                .build()
                .toString()
    }

    fun trendingUrl(
            page: Int = 1,
            language: String = "en-US",
            timeWindow: String = "day"
    ): String {
        return getUriBuilder()
                .appendPath("trending")
                .appendPath("movie")
                .appendPath(timeWindow)
                .appendQueryParameter("page", page.toString())
                .appendQueryParameter("language", language)
                .build()
                .toString()
    }

    fun searchUrl(
            query: String,
            language: String = "en-US",
            page: Int = 1
    ): String {
        return getUriBuilder()
                .appendPath("search")
                .appendPath("movie")
                .appendQueryParameter("query", query)
                .appendQueryParameter("language", language)
                .appendQueryParameter("include_adult", "false")
                .appendQueryParameter("page", page.toString())
                .build()
                .toString()
    }

    private fun getUriBuilder() =
            Uri.Builder()
                .scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendQueryParameter("api_key", BuildConfig.MovieDBApiKey)
}