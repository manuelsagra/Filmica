package com.manuelsagra.filmica.view.films

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.manuelsagra.filmica.R
import com.manuelsagra.filmica.data.DiscoverRepo
import com.manuelsagra.filmica.data.Film
import com.manuelsagra.filmica.view.utils.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_films.*
import kotlinx.android.synthetic.main.layout_error.*

open class FilmsFragment: Fragment() {
    lateinit var listener: OnItemClickListener

    open val list: RecyclerView by lazy {
        val instance = view!!.findViewById<RecyclerView>(R.id.list_films_discover)
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
        btnRetry.setOnClickListener {
            reload()
        }

        list.adapter = adapter
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnItemClickListener) {
            listener = context
        }
    }

    override fun onResume() {
        super.onResume()
        reload()
    }

    fun reload() {
        Log.i("RELOAD DISCOVER", "Called")

        DiscoverRepo.discoverFilms(context!!, { films ->
            Log.i("RELOAD DISCOVER", "Finished")
            progressBar.visibility = View.INVISIBLE
            layoutError.visibility = View.INVISIBLE
            list.visibility = View.VISIBLE

            adapter.setFilms(films)
        }, { error ->
            progressBar.visibility = View.INVISIBLE
            layoutError.visibility = View.VISIBLE
            list.visibility = View.INVISIBLE

            error.printStackTrace()
        })
    }

    interface OnItemClickListener {
        fun onItemClicked(film: Film)
    }
}