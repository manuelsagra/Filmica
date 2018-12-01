package com.manuelsagra.filmica.view.films

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.manuelsagra.filmica.R
import com.manuelsagra.filmica.data.Film
import com.manuelsagra.filmica.view.details.DetailsActivity
import com.manuelsagra.filmica.view.details.DetailsFragment
import com.manuelsagra.filmica.view.details.DetailsPlaceholderFragment
import com.manuelsagra.filmica.view.search.SearchFragment
import com.manuelsagra.filmica.view.trending.TrendingFragment
import com.manuelsagra.filmica.view.utils.FilmClickListener
import com.manuelsagra.filmica.view.watchlist.WatchlistFragment
import kotlinx.android.synthetic.main.activity_films.*

const val TAG_FILMS = "filmsTag"
const val TAG_TRENDING = "trendingTag"
const val TAG_WATCHLIST = "watchlistTag"
const val TAG_SEARCH = "searchTag"

class FilmsActivity: AppCompatActivity(), FilmClickListener {
    private lateinit var filmsFragment: FilmsFragment
    private lateinit var trendingFragment: TrendingFragment
    private lateinit var watchlistFragment: WatchlistFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        if (savedInstanceState == null) {
            setupFragments()
        } else {
            restoreFragments(savedInstanceState)
        }

        navigation?.setOnNavigationItemSelectedListener { item ->
            val id = item.itemId

            when (id) {
                R.id.action_trending -> showMainFragment(trendingFragment)
                R.id.action_discover -> showMainFragment(filmsFragment)
                R.id.action_watchlist -> showMainFragment(watchlistFragment)
                R.id.action_search -> showMainFragment(searchFragment)
            }

            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("active", activeFragment.tag)
    }

    private fun setupFragments() {
        filmsFragment = FilmsFragment()
        trendingFragment = TrendingFragment()
        watchlistFragment = WatchlistFragment()
        searchFragment = SearchFragment()

        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_films, trendingFragment, TAG_TRENDING)
                .add(R.id.fragment_films, filmsFragment, TAG_FILMS)
                .add(R.id.fragment_films, watchlistFragment, TAG_WATCHLIST)
                .add(R.id.fragment_films, searchFragment, TAG_SEARCH)
                .hide(filmsFragment)
                .hide(watchlistFragment)
                .hide(searchFragment)
                .commit()

        if (isTablet()) {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentDetails, DetailsPlaceholderFragment()).commit()
        }

        activeFragment = trendingFragment
    }

    private fun restoreFragments(savedInstanceState: Bundle) {
        filmsFragment = supportFragmentManager.findFragmentByTag(TAG_FILMS) as FilmsFragment
        trendingFragment = supportFragmentManager.findFragmentByTag(TAG_TRENDING) as TrendingFragment
        watchlistFragment = supportFragmentManager.findFragmentByTag(TAG_WATCHLIST) as WatchlistFragment
        searchFragment = supportFragmentManager.findFragmentByTag(TAG_SEARCH) as SearchFragment
        activeFragment = supportFragmentManager.findFragmentByTag(savedInstanceState.getString("active"))
    }

    override fun onItemClicked(film: Film) {
        showDetails(film.id)
    }

    private fun showMainFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .commit()

        activeFragment = fragment
    }

    fun showDetails(filmId: String) {
        if (isTablet()) {
            showDetailsFragment(filmId)
        } else {
            launchDetailsActivity(filmId)
        }
    }

    private fun isTablet() = this.fragmentDetails != null

    private fun launchDetailsActivity(filmId: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("filmId", filmId)
        startActivity(intent)
    }

    private fun showDetailsFragment(filmId: String) {
        val fragment = DetailsFragment.newInstance(filmId)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentDetails, fragment)
                .commit()
    }
}