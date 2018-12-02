package com.manuelsagra.filmica.view.watchlist

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.manuelsagra.filmica.R
import com.manuelsagra.filmica.data.Film
import com.manuelsagra.filmica.data.FilmsRepo
import com.manuelsagra.filmica.view.utils.FilmClickListener
import com.manuelsagra.filmica.view.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_watchlist.*

class WatchlistFragment : Fragment() {
    lateinit var listener: FilmClickListener

    val adapter: WatchlistAdapter by lazy {
        val instance = WatchlistAdapter() { film ->
            listener.onItemClicked(film)
        }

        instance
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is FilmClickListener) {
            listener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        watchlist.adapter = adapter

        setupSwipeHandler()
    }

    override fun onResume() {
        super.onResume()

        FilmsRepo.watchlist(context!!) { films ->
            adapter.setFilms(films.toMutableList())
        }
    }

    private fun setupSwipeHandler() {
        val swipeHandler = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteFilmAt(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(watchlist)
    }

    private fun deleteFilmAt(position: Int) {
        val film = adapter.getFilm(position)
        deleteFilm(film)
    }

    private fun deleteFilm(film: Film) {
        FilmsRepo.deleteFilm(context!!, film) { film ->
            update()
            Snackbar.make(view!!, R.string.removed_to_watchlist, Snackbar.LENGTH_LONG).setAction(getString(R.string.undo), {
                saveFilm(film)
            }).show()
        }
    }

    private fun saveFilm(film: Film) {
        FilmsRepo.saveFilm(context!!, film) {
            update()
            Snackbar.make(view!!, R.string.added_to_watchlist, Snackbar.LENGTH_LONG).setAction(getString(R.string.undo), {
                deleteFilm(film)
            }).show()
        }
    }

    fun update() {
        FilmsRepo.watchlist(context!!) { films ->
            adapter.setFilms(films.toMutableList())
        }
    }
}
