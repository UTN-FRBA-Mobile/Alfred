package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2

import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.Permissions.Permissions
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.Api
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.responses.Movie
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.responses.MovieSearch
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.AppDatabase
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.daos.FavoriteMoviesDao
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
    private lateinit var favoriteMoviesDao: FavoriteMoviesDao

    private lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        api = createNetworkClient(Api::class.java, "http://www.omdbapi.com")

        movieSearchDao = AppDatabase.getInstance(this@MainActivity).movieSearchDao()
        favoriteMoviesDao = AppDatabase.getInstance(this@MainActivity).favoriteMoviesDao()

        SetUpUIElements()

        Toast.makeText(this, "Hola " + MyPreferences.getUsername(this), Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val FIRST_POSITION = 0;
        if (requestCode == Permissions.REQUEST_WRITE_EXTERNAL_STORAGE && grantResults.isNotEmpty() && grantResults[FIRST_POSITION] == PackageManager.PERMISSION_GRANTED) {
            loadFavorites()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

        val preferredSearchLayoutType = MyPreferences.getPreferredSearchView(this@MainActivity)
        if(preferredSearchLayoutType == MovieAdapter.Layout_list){
            listView.visibility = View.GONE
            gridView.visibility = View.VISIBLE
        } else {
            listView.visibility = View.VISIBLE
            gridView.visibility = View.GONE
        }

        listView.setOnClickListener {
            recyclerView.layoutManager = LinearLayoutManager(this)
            MyPreferences.setPreferredSearchView(this, MovieAdapter.Layout_list)
            (recyclerView.adapter as MovieAdapter).layoutType = MovieAdapter.Layout_list
            listView.visibility = View.GONE
            gridView.visibility = View.VISIBLE
        }

        gridView.setOnClickListener {
            recyclerView.layoutManager = GridLayoutManager(this, 3)
            MyPreferences.setPreferredSearchView(this, MovieAdapter.Layout_grid)
            (recyclerView.adapter as MovieAdapter).layoutType = MovieAdapter.Layout_grid
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

                val preferredSearchLayoutType = MyPreferences.getPreferredSearchView(this@MainActivity)
                recyclerView.layoutManager = if (preferredSearchLayoutType == MovieAdapter.Layout_grid) GridLayoutManager(this@MainActivity, 3) else LinearLayoutManager(this@MainActivity)
                adapter = MovieAdapter(this@MainActivity, movies, preferredSearchLayoutType, false)
                recyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<MovieSearch>, t: Throwable) {
                showError(t)
            }
        })
    }

    private fun favMovie() {
        val callbackOnSuccess = object: Permissions.Callback {
            override fun onSuccess() {
                loadFavorites()
            }

        }
        Permissions.checkForPermissions(this@MainActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.external_storage_permission_reason), callbackOnSuccess)
    }

    private fun loadFavorites() {
        setLoading(true)
        class SetFavoriteMovieAsync : AsyncTask<Void, Void, List<ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.responses.Movie>>() {
            override fun doInBackground(vararg params: Void): List<ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.responses.Movie> {
                return favoriteMoviesDao.getAll().map { fm -> Movie().apply {
                    title = fm.title
                    poster = fm.poster
                    year = fm.year
                } }
            }

            override fun onPostExecute(starredMovies: List<ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.responses.Movie>) {
                val preferredSearchLayoutType = MyPreferences.getPreferredSearchView(this@MainActivity)
                recyclerView.layoutManager = if(preferredSearchLayoutType == MovieAdapter.Layout_grid) GridLayoutManager(this@MainActivity, 3) else LinearLayoutManager(this@MainActivity)
                adapter = MovieAdapter(this@MainActivity, starredMovies, preferredSearchLayoutType, true)
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
