package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.GridLayout
import android.widget.Toast
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.Api
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.responses.Movie
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.responses.MovieSearch
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.AppDatabase
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.daos.FavoritesDao
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.daos.SearchesDao
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.entities.Search
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.preferences.MyPreferences
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity(), SearchedMoviesAdapter.IListener {
    internal lateinit var adapter: MovieAdapter
    private lateinit var searchedMoviesAdapter: SearchedMoviesAdapter

    private lateinit var movieSearchDao: SearchesDao

    private lateinit var api: Api

    private var searchLayoutType: Int = MovieAdapter.Layout_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        api = createNetworkClient(Api::class.java, "http://www.omdbapi.com")

        movieSearchDao = AppDatabase.getInstance(this@MainActivity).movieSearchDao()

        // Obtain layout type from persistence
        searchLayoutType = MyPreferences.getLayoutType(this@MainActivity)
        SetUpUIElements()

        Toast.makeText(this, "Hola " + MyPreferences.getUsername(this), Toast.LENGTH_LONG).show()
    }

    private fun SetUpUIElements() {
        titleInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    showHistory(s.toString())
                } else {
                    hideHistory()
                }
            }
        })

        findButton.setOnClickListener { findMovie() }

        favButton.setOnClickListener { favMovie() }

        listView.setOnClickListener {
            recyclerView.layoutManager = LinearLayoutManager(this)
            (recyclerView.adapter as MovieAdapter).layoutType = MovieAdapter.Layout_list
            searchLayoutType = MovieAdapter.Layout_list

            //Persist Layout type
            MyPreferences.setLayoutType(this@MainActivity, searchLayoutType)

            listView.visibility = View.GONE
            gridView.visibility = View.VISIBLE
        }

        gridView.setOnClickListener {
            recyclerView.layoutManager = GridLayoutManager(this, 3)
            (recyclerView.adapter as MovieAdapter).layoutType = MovieAdapter.Layout_grid
            searchLayoutType = MovieAdapter.Layout_grid
            
            //Persist Layout type
            MyPreferences.setLayoutType(this@MainActivity, searchLayoutType)

            gridView.visibility = View.GONE
            listView.visibility = View.VISIBLE
        }
    }

    internal fun findMovie() {
        val title = titleInput.text.toString()

        findMovie(title)
    }

    internal fun findMovie(movieTitle: String) {
        setLoading(true)
        val apiKey = "7a9f6b43"

        api.findMovie(apiKey, movieTitle).enqueue(object : Callback<MovieSearch> {
            override fun onResponse(call: Call<MovieSearch>, response: Response<MovieSearch>) {
                val movies = ArrayList<ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.responses.Movie>()
                if (response.isSuccessful) {
                    class AddToHistoryAsync : AsyncTask<Void, Void, Unit>() {
                        override fun doInBackground(vararg params: Void) {
                            movieSearchDao.insert(Search().apply {
                                title = movieTitle
                                dateOfSearch = Date()
                            })
                        }

                        override fun onPostExecute(response: Unit) {
                        }
                    }

                    AddToHistoryAsync().execute()

                    if (response.body() != null && response.body()!!.search != null) {
                        movies.addAll(response.body()!!.search!!)
                    }
                } else {
                    showError("Error...")
                }

                setLoading(false)

                recyclerView.layoutManager = if (searchLayoutType == MovieAdapter.Layout_grid) GridLayoutManager(this@MainActivity, 3) else LinearLayoutManager(this@MainActivity)
                adapter = MovieAdapter(this@MainActivity, movies, searchLayoutType, false)
                recyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<MovieSearch>, t: Throwable) {
                showError(t)
            }
        })
    }

    private fun favMovie() {
        setLoading(true)
        class SetFavoriteMovieAsync : AsyncTask<Void //entrada
        , Void //actualizacion de progreso
        , List<Movie>// Salida
        >() {
            override fun doInBackground(vararg params: Void):List<Movie> {
                // Obtain Favorite movies from storage

                // Map favorites to movies
                return AppDatabase.getInstance(this@MainActivity).favoriteSearchDao().find_all().map { Movie(it.title, it.year, it.poster, it.metascore) }
            }

            override fun onPostExecute(starredMovies: List<Movie>) {
                recyclerView.layoutManager = if(searchLayoutType == MovieAdapter.Layout_grid) LinearLayoutManager(this@MainActivity) else GridLayoutManager(this@MainActivity, 3)
                adapter = MovieAdapter(this@MainActivity, starredMovies, searchLayoutType, true)
                recyclerView.adapter = adapter

                setLoading(false)
            }
        }

        SetFavoriteMovieAsync().execute()
    }

    internal fun showHistory(termSearched: String) {
        class GetHistoryAsync : AsyncTask<Void, Void, List<String>>() {
            override fun doInBackground(vararg params: Void): List<String> {
                return movieSearchDao.getCoincidences(termSearched).map { ms -> ms.title }
            }

            override fun onPostExecute(searchedMovies: List<String>) {
                if (searchedMovies.isEmpty()) {
                    hideHistory()
                    return
                }

                searchRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                searchedMoviesAdapter = SearchedMoviesAdapter(this@MainActivity, searchedMovies)
                searchRecyclerView.adapter = searchedMoviesAdapter
                searchRecyclerView.visibility = View.VISIBLE
            }
        }

        GetHistoryAsync().execute()
    }

    internal fun hideHistory() {
        searchRecyclerView.adapter = null
        searchRecyclerView.visibility = View.GONE
    }

    internal fun setLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        findButton.isEnabled = !isLoading
        favButton.isEnabled = !isLoading
    }

    internal fun showError(error: Throwable) {
        showError(error.toString())
    }

    internal fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun <T> createNetworkClient(apiClass: Class<T>, baseUrl: String): T {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder().addInterceptor(interceptor)
        val client = builder.build()

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                GsonBuilder()
                                        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                                        .create()
                        ))
                .build()

        return retrofit.create(apiClass)
    }

    override fun itemClicked(title: String) {
        hideHistory()
        findMovie(title)
    }
}
