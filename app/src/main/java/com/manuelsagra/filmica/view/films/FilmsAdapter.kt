package com.manuelsagra.filmica.view.films

import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.view.View
import com.manuelsagra.filmica.R
import com.manuelsagra.filmica.data.Film
import com.manuelsagra.filmica.view.utils.BaseFilmHolder
import com.manuelsagra.filmica.view.utils.PagedFilmAdapter
import com.manuelsagra.filmica.view.utils.SimpleTarget
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_film.view.*

class FilmsAdapter(
        clickListener: ((Film) -> Unit)? = null
): PagedFilmAdapter<FilmsAdapter.FilmViewHolder>(
        layoutItem = R.layout.item_film,
        holderCreator = { view -> FilmsAdapter.FilmViewHolder(view, clickListener) }
) {
    class FilmViewHolder(itemView: View, clickListener: ((Film) -> Unit)?): BaseFilmHolder(itemView, clickListener) {
        override fun bindFilm(film: Film) {
            super.bindFilm(film)

            with(itemView) {
                labelTitle.text = film.title
                labelGenre.text = film.genre
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
                var darkSwatch = palette?.darkVibrantSwatch ?: palette?.dominantSwatch
                val darkColour = darkSwatch?.rgb
                        ?: ContextCompat.getColor(itemView.context, R.color.colorPrimaryDark)

                var lightSwatch = palette?.lightVibrantSwatch ?: palette?.lightMutedSwatch
                val lightColour = lightSwatch?.rgb
                        ?: ContextCompat.getColor(itemView.context, R.color.colorPrimary)

                itemView.container.setBackgroundColor(lightColour)
                itemView.containerData.setBackgroundColor(darkColour)
            }
        }
    }
}