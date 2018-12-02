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
import com.manuelsagra.filmica.view.discover.DiscoverFragment
import com.manuelsagra.filmica.view.search.SearchFragment
import com.manuelsagra.filmica.view.trending.TrendingFragment
import com.manuelsagra.filmica.view.utils.FilmClickListener
import com.manuelsagra.filmica.view.watchlist.WatchlistFragment
import kotlinx.android.synthetic.main.activity_films.*

const val TAG_DISCOVER = "discoverTag"
const val TAG_TRENDING = "trendingTag"
const val TAG_WATCHLIST = "watchlistTag"
const val TAG_SEARCH = "searchTag"
const val TAG_DETAILS = "detailsTag"

class FilmsActivity: AppCompatActivity(), FilmClickListener, DetailsFragment.WatchlistUpdateListener {
    private lateinit var discoverFragment: DiscoverFragment
    private lateinit var trendingFragment: TrendingFragment
    private lateinit var watchlistFragment: WatchlistFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var activeFragment: Fragment

    // Life cycle
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
                R.id.action_discover -> showMainFragment(discoverFragment)
                R.id.action_watchlist -> showMainFragment(watchlistFragment)
                R.id.action_search -> showMainFragment(searchFragment)
            }

            if (isTablet()) {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentDetails, DetailsPlaceholderFragment()).commit()
            }

            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("active", activeFragment.tag)
    }

    private fun setupFragments() {
        discoverFragment = DiscoverFragment()
        trendingFragment = TrendingFragment()
        watchlistFragment = WatchlistFragment()
        searchFragment = SearchFragment()

        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_films, trendingFragment, TAG_TRENDING)
                .add(R.id.fragment_films, discoverFragment, TAG_DISCOVER)
                .add(R.id.fragment_films, watchlistFragment, TAG_WATCHLIST)
                .add(R.id.fragment_films, searchFragment, TAG_SEARCH)
                .hide(discoverFragment)
                .hide(watchlistFragment)
                .hide(searchFragment)
                .commit()

        if (isTablet()) {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentDetails, DetailsPlaceholderFragment()).commit()
        }

        activeFragment = trendingFragment
    }

    private fun restoreFragments(savedInstanceState: Bundle) {
        discoverFragment = supportFragmentManager.findFragmentByTag(TAG_DISCOVER) as DiscoverFragment
        trendingFragment = supportFragmentManager.findFragmentByTag(TAG_TRENDING) as TrendingFragment
        watchlistFragment = supportFragmentManager.findFragmentByTag(TAG_WATCHLIST) as WatchlistFragment
        searchFragment = supportFragmentManager.findFragmentByTag(TAG_SEARCH) as SearchFragment
        activeFragment = supportFragmentManager.findFragmentByTag(savedInstanceState.getString("active"))

        if (isTablet()) {
            if (supportFragmentManager.findFragmentByTag(TAG_DETAILS) != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentDetails, supportFragmentManager.findFragmentByTag(TAG_DETAILS)).commit()
            } else {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentDetails, DetailsPlaceholderFragment()).commit()
            }
        }
    }

    private fun showMainFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .commit()

        activeFragment = fragment
    }

    // Details
    private fun showDetails(filmId: String) {
        if (isTablet()) {
            showDetailsFragment(filmId)
        } else {
            launchDetailsActivity(filmId)
        }
    }

    private fun launchDetailsActivity(filmId: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("filmId", filmId)
        startActivity(intent)
    }

    private fun showDetailsFragment(filmId: String) {
        val fragment = DetailsFragment.newInstance(filmId)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentDetails, fragment, TAG_DETAILS)
                .commit()
    }

    // Listeners
    override fun onItemClicked(film: Film) {
        showDetails(film.id)
    }

    override fun onWatchlistUpdate() {
        if (isTablet()) {
            watchlistFragment.update()
        }
    }

    // Aux methods
    private fun isTablet() = this.fragmentDetails != null

}