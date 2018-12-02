package com.manuelsagra.filmica.data

import android.arch.paging.DataSource
import android.content.Context

class TrendingDataSourceFactory(
        context: Context,
        language: String
): DataSource.Factory<Int, Film>() {
    private val context = context
    private val language = language

    override fun create(): DataSource<Int, Film> {
        return TrendingDataSource(context, language)
    }
}