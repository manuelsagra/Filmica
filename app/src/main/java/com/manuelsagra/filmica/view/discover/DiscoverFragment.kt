package com.manuelsagra.filmica.view.discover

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.manuelsagra.filmica.R
import com.manuelsagra.filmica.data.DiscoverDataSourceFactory
import com.manuelsagra.filmica.data.Film
import com.manuelsagra.filmica.data.TrendingDataSourceFactory
import com.manuelsagra.filmica.view.films.FilmsAdapter
import com.manuelsagra.filmica.view.trending.PAGE_SIZE
import com.manuelsagra.filmica.view.utils.FilmClickListener
import com.manuelsagra.filmica.view.utils.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_films.*

open class DiscoverFragment: Fragment() {
    lateinit var listener: FilmClickListener
    lateinit private var filmList: LiveData<PagedList<Film>>

    open val list: RecyclerView by lazy {
        val instance = view!!.findViewById<RecyclerView>(R.id.listFilmsDiscover)
        instance.addItemDecoration(ItemOffsetDecoration(R.dimen.grid_offset))
        instance.setHasFixedSize(true)

        instance
    }

    val adapter: FilmsAdapter by lazy {
        val instance = FilmsAdapter() { film ->
            listener.onItemClicked(film)
        }

        instance
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_films, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.adapter = adapter

        val config = PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setInitialLoadSizeHint(PAGE_SIZE)
                .setEnablePlaceholders(false)
                .build()
        val filmDataSourceFactory = DiscoverDataSourceFactory(context!!)
        filmList = LivePagedListBuilder<Int, Film>(filmDataSourceFactory, config).build()

        filmList.observe(this, Observer { list ->
            Log.i("Discover", "Changed")
            progressBar.visibility = View.GONE
            listFilmsDiscover.visibility = View.VISIBLE
            adapter.submitList(list)
        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is FilmClickListener) {
            listener = context
        }
    }
}