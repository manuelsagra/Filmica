package com.manuelsagra.filmica.view.details

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.view.*
import android.widget.Toast
import com.manuelsagra.filmica.R
import com.manuelsagra.filmica.data.Film
import com.manuelsagra.filmica.data.FilmsRepo
import com.manuelsagra.filmica.view.utils.SimpleTarget
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment: Fragment() {

    private var film: Film? = null

    companion object {
        fun newInstance(filmId: String): DetailsFragment {
            val instance = DetailsFragment()
            val args = Bundle()
            args.putString("filmId", filmId)
            instance.arguments = args

            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filmId: String = arguments?.getString("filmId") ?: ""
        film = FilmsRepo.findFilmById(filmId)

        film?.let {
            with(it) {
                labelTitle.text = title
                labelRating.text = rating.toString()
                labelOverview.text = overview
                labelGenre.text = genre
                labelRelease.text = releaseDate

                loadImage(it)
            }
        }

        btnAdd.setOnClickListener {
            film?.let {
                FilmsRepo.saveFilm(context!!, it) {
                    Toast.makeText(context,"Added to watchlist", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item.takeIf { item?.itemId == R.id.action_share }?.let { menuItem ->
            shareFilm()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun shareFilm() {
        val intent = Intent(Intent.ACTION_SEND)

        film?.let {
            val text = getString(R.string.template_share, it.title, it.rating)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
        }

        startActivity(Intent.createChooser(intent, getString(R.string.title_share)))
    }

    private fun loadImage(film: Film) {
        val target = SimpleTarget(
                successCallback = { bitmap, from ->
                    imgPoster.setImageBitmap(bitmap)
                    setColourFrom(bitmap)
                }
        )
        imgPoster.tag = target

        Picasso.get()
                .load(film.getPosterURL())
                .error(R.drawable.placeholder)
                .into(target)
    }

    private fun setColourFrom(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            var darkSwatch = palette?.darkVibrantSwatch ?: palette?.dominantSwatch
            val darkColour = darkSwatch?.rgb
                    ?: ContextCompat.getColor(context!!, R.color.colorPrimaryDark)

            var lightSwatch = palette?.lightVibrantSwatch ?: palette?.lightMutedSwatch
            val lightColour = lightSwatch?.rgb
                    ?: ContextCompat.getColor(context!!, R.color.colorPrimary)

            var overlayColour = Color.argb(
                    (Color.alpha(lightColour) * 0.5).toInt(),
                    Color.red(lightColour),
                    Color.green(lightColour),
                    Color.blue(lightColour)
            )
            overlay.setBackgroundColor(overlayColour)
            btnAdd.backgroundTintList = ColorStateList.valueOf(darkColour)
        }
    }
}