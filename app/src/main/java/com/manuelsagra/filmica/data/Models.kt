package com.manuelsagra.filmica.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

const val MAX_SEARCH_RESULTS = 10

@Entity
data class Film(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var genre: String = "",
    @ColumnInfo(name="release_date") var releaseDate: String = "",
    var rating: Double = 0.0,
    var overview: String = "",
    var poster: String = ""
) {

    @Ignore
    constructor(): this("")

    fun getPosterURL(): String {
        return "$POSTER_BASE_URL$poster"
    }

    companion object {
        fun parseFilms(response: JSONObject): MutableList<Film> {
            val parsedFilms: MutableList<Film> = mutableListOf()

            val results = response.getJSONArray("results")

            if (results != null) {
                val size = if (results.length() > MAX_SEARCH_RESULTS) MAX_SEARCH_RESULTS else results.length()
                for (i in 0..(size - 1)) {
                    parsedFilms.add(parseFilm(results.getJSONObject(i)))
                }
            }

            return parsedFilms
        }

        fun parseFilm(jsonFilm: JSONObject): Film {
            return Film(
                    id = jsonFilm.getLong("id").toString(),
                    title = jsonFilm.getString("title"),
                    releaseDate = jsonFilm.getString("release_date"),
                    rating = jsonFilm.getDouble("vote_average"),
                    overview = jsonFilm.getString("overview") ?: "",
                    genre = getGenres(jsonFilm.getJSONArray("genre_ids")),
                    poster = jsonFilm.optString("poster_path", "")
            )
        }

        private fun getGenres(genresArray: JSONArray): String {
            var genres = mutableListOf<String>()

            if (genresArray != null) {
                for (i in 0..(genresArray.length() - 1)) {
                    val gid = genresArray.getInt(i)
                    val genre = ApiConstants.genres[gid] ?: ""
                    genres.add(genre)
                }
            }

            return if (genres.size > 0) genres.reduce { acc, genre -> "$acc | $genre" } else ""
        }
    }
}

