package com.manuelsagra.filmica.view.watchlist

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.view.View
import com.manuelsagra.filmica.R
import com.manuelsagra.filmica.data.Film
import com.manuelsagra.filmica.view.utils.BaseFilmAdapter
import com.manuelsagra.filmica.view.utils.BaseFilmHolder
import com.manuelsagra.filmica.view.utils.SimpleTarget
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_watchlist.view.*

class WatchlistAdapter: BaseFilmAdapter<WatchlistAdapter.WatchlistHolder> (
        layoutItem = R.layout.item_watchlist,
        holderCreator = { view -> WatchlistHolder(view) }
) {
    class WatchlistHolder(itemView: View): BaseFilmHolder(itemView) {
        override fun bindFilm(film: Film) {
            super.bindFilm(film)

            with(itemView) {
                labelTitle.text = film.title
                labelOverview.text = film.overview
                labelRating.text = film.rating.toString()
                loadImage(film)
            }
        }

        private fun loadImage(film: Film) {
            val target = SimpleTarget(
                    successCallback = { bitmap, from ->
                        itemView.imgPoster.setImageBitmap(bitmap)
                        setColourFrom(bitmap)
                    }
            )
            itemView.imgPoster.tag = target

            Picasso.get()
                    .load(film.getPosterURL())
                    .error(R.drawable.placeholder)
                    .into(target)
        }

        private fun setColourFrom(bitmap: Bitmap) {
            Palette.from(bitmap).generate { palette ->
                var lightSwatch = palette?.lightVibrantSwatch ?: palette?.lightMutedSwatch
                val lightColour = lightSwatch?.rgb
                        ?: ContextCompat.getColor(itemView.context!!, R.color.colorPrimary)

                var overlayColour = Color.argb(
                        (Color.alpha(lightColour) * 0.5).toInt(),
                        Color.red(lightColour),
                        Color.green(lightColour),
                        Color.blue(lightColour)
                )

                itemView.imgOverlay.setBackgroundColor(overlayColour)
            }
        }
    }
}