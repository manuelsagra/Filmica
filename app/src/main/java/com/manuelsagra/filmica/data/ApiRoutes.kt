package com.manuelsagra.filmica.data

import android.net.Uri
import com.manuelsagra.filmica.BuildConfig

object ApiRoutes {
    fun discoverUrl(
            language: String = "en-US",
            sortBy: String = "popularity.desc",
            page: Int = 1
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

    private fun getUriBuilder() =
            Uri.Builder()
                .scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendQueryParameter("api_key", BuildConfig.MovieDBApiKey)
}