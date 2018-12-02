package com.manuelsagra.filmica.view.search

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.manuelsagra.filmica.R
import com.manuelsagra.filmica.data.FilmsRepo
import com.manuelsagra.filmica.view.utils.FilmClickListener
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_noresults.*

const val MIN_SEARCH_QUERY = 3

class SearchFragment : Fragment() {
    private var stopSearch: Boolean = false
    lateinit var listener: FilmClickListener

    val adapter: SearchAdapter by lazy {
        val instance = SearchAdapter() { film ->
            listener.onItemClicked(film)
        }

        instance
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is FilmClickListener) {
            listener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listResults.adapter = adapter

        txtQuery.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val query = s.toString()
                    if (query.length > MIN_SEARCH_QUERY) {
                        stopSearch = false
                        progressBar.visibility = View.VISIBLE
                        search(query)
                    } else {
                        stopSearch = true
                        progressBar.visibility = View.GONE
                    }
                    layoutError.visibility = View.GONE
                    layoutNoResults.visibility = View.GONE
                    listResults.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    fun search(query: String) {
        FilmsRepo.searchFilms(context!!, query, getString(R.string.lang), { films ->
            if (!stopSearch && films.size > 0) {
                progressBar.visibility = View.GONE
                layoutError.visibility = View.GONE
                layoutNoResults.visibility = View.GONE
                listResults.visibility = View.VISIBLE
                adapter.setFilms(films)
            } else if (films.size == 0) {
                progressBar.visibility = View.GONE
                layoutError.visibility = View.GONE
                layoutNoResults.visibility = View.VISIBLE
                listResults.visibility = View.GONE
            }
        }, { error ->
            progressBar.visibility = View.GONE
            layoutError.visibility = View.VISIBLE
            layoutNoResults.visibility = View.GONE
            listResults.visibility = View.GONE

            error.printStackTrace()
        })
    }
}
