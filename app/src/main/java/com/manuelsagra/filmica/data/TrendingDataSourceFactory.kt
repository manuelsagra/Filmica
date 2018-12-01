package com.manuelsagra.filmica.data

import android.arch.paging.DataSource
import android.content.Context

class TrendingDataSourceFactory(
        context: Context
): DataSource.Factory<Int, Film>() {
    private val context = context

    override fun create(): DataSource<Int, Film> {
        return TrendingDataSource(context)
    }
}