package com.manuelsagra.filmica.view.details

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.manuelsagra.filmica.R

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        if (savedInstanceState == null) {
            val filmId = intent.getStringExtra("filmId")

            val detailsFragment = DetailsFragment.newInstance(filmId)

            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_details, detailsFragment)
                    .commit()
        }
    }
}
