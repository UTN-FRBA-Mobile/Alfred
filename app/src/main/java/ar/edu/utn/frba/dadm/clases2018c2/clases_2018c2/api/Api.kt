package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api

import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.responses.MovieSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/")
    fun findMovie(
            @Query("apiKey") apiKey: String,
            @Query("s") title: String): Call<MovieSearch>
}