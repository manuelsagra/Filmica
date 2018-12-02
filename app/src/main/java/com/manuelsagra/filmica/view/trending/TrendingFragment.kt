package com.manuelsagra.filmica.view.trending

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.manuelsagra.filmica.R
import com.manuelsagra.filmica.data.Film
import com.manuelsagra.filmica.data.PAGE_SIZE
import com.manuelsagra.filmica.data.TrendingDataSourceFactory
import com.manuelsagra.filmica.view.films.FilmsAdapter
import com.manuelsagra.filmica.view.utils.FilmClickListener
import com.manuelsagra.filmica.view.utils.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_trending.*

open class TrendingFragment: Fragment() {
    lateinit private var listener: FilmClickListener
    lateinit private var filmList: LiveData<PagedList<Film>>

    private val list: RecyclerView by lazy {
        val instance = view!!.findViewById<RecyclerView>(R.id.listFilmsTrending)
        instance.addItemDecoration(ItemOffsetDecoration(R.dimen.grid_offset))
        instance.setHasFixedSize(true)

        instance
    }

    private val adapter: FilmsAdapter by lazy {
        val instance = FilmsAdapter() { film ->
            listener.onItemClicked(film)
        }

        instance
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.adapter = adapter

        val config = PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setInitialLoadSizeHint(PAGE_SIZE)
                .setEnablePlaceholders(false)
                .build()
        val filmDataSourceFactory = TrendingDataSourceFactory(context!!, getString(R.string.lang))
        filmList = LivePagedListBuilder<Int, Film>(filmDataSourceFactory, config).build()

        filmList.observe(this, Observer { list ->
            adapter.submitList(list)
            progressBar.visibility = View.GONE
            listFilmsTrending.visibility = View.VISIBLE
        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is FilmClickListener) {
            listener = context
        }
    }
}