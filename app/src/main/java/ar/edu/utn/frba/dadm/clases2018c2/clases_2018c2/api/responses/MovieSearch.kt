package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.responses

import java.io.Serializable

class MovieSearch : Serializable {
    var response: Boolean = false
    var search: List<Movie>? = null
    var totalResults: String? = null
}