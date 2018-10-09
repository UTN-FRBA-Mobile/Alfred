package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.Permissions.Permissions
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.AppDatabase
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.entities.FavoriteMovie
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.fileSystem.ExternalStorage
import com.squareup.picasso.Picasso
import java.lang.Exception

class MovieViewHolder(itemView: View, val context: Context, private val storePostersInFs: Boolean) : RecyclerView.ViewHolder(itemView) {
    private val poster: ImageView = itemView.findViewById(R.id.moviePoster)
    val title: TextView? = itemView.findViewById(R.id.movieTitle)
    private val year: TextView? = itemView.findViewById(R.id.movieYear)
    val setStarred: ImageView? = itemView.findViewById(R.id.set_starred)
    val unsetStarred: ImageView? = itemView.findViewById(R.id.unset_starred)
    val favoriteMoviesDao = AppDatabase.getInstance(context).favoriteMoviesDao()

    fun bind(movie: ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.responses.Movie) {
        class BindAsync : AsyncTask<Void, Void, FavoriteMovie?>() {
            override fun doInBackground(vararg params: Void): FavoriteMovie? {
                return favoriteMoviesDao.getByTitle(movie.title!!)
            }

            override fun onPostExecute(response: FavoriteMovie?) {
                if(response == null){
                    setStarred?.visibility = View.VISIBLE
                    unsetStarred?.visibility = View.GONE
                } else {
                    setStarred?.visibility = View.GONE
                    unsetStarred?.visibility = View.VISIBLE
                }

                setUpSetStarred(movie)

                setUpUnsetStarred(movie)
            }
        }

        title?.text = movie.title
        year?.text = movie.year

        val fsPosterUri = ExternalStorage.getFileUri(movie.title!!)
        //val fsPosterUri = ExternalStorage.getCacheFileUri(context, movie.title!!)
        //val fsPosterUri = InternalStorage.getFileUri(context, movie.title!!)
        //val fsPosterUri = InternalStorage.getCacheFileUri(context, movie.title!!)
        if(fsPosterUri == null){
            if(storePostersInFs){
                Picasso.get().load(movie.poster).placeholder(android.R.drawable.ic_media_play).into(getTarget(poster, movie.title!!))
            } else {
                Picasso.get().load(movie.poster).placeholder(android.R.drawable.ic_media_play).into(poster)
            }
        } else {
            Picasso.get().load(fsPosterUri).placeholder(android.R.drawable.ic_media_play).into(poster)
        }

        BindAsync().execute()
    }

    private fun setUpSetStarred(movie: ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.responses.Movie) {
        class SetUpSetStarredAsync : AsyncTask<Void, Void, Unit>() {
            override fun doInBackground(vararg params: Void) {
                favoriteMoviesDao.insert(FavoriteMovie().apply {
                    this.poster = movie.poster
                    this.year = movie.year
                    this.title = movie.title!!
                })
            }

            override fun onPostExecute(response: Unit) {
                setStarred?.visibility = View.GONE
                unsetStarred?.visibility = View.VISIBLE
            }
        }

        setStarred?.setOnClickListener {
            SetUpSetStarredAsync().execute()
        }
    }

    private fun setUpUnsetStarred(movie: ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.responses.Movie) {
        class SetUpUnsetStarredAsync : AsyncTask<Void, Void, Unit>() {
            override fun doInBackground(vararg params: Void) {
                favoriteMoviesDao.delete(FavoriteMovie().apply {
                    this.poster = movie.poster
                    this.year = movie.year
                    this.title = movie.title!!
                })

                ExternalStorage.deleteFile(movie.title!!)
                //ExternalStorage.deleteFileFromCache(context, fileName)
                //InternalStorage.deleteFile(context, fileName)
                //InternalStorage.deleteFileFromCache(context, fileName)
            }

            override fun onPostExecute(response: Unit) {
                setStarred?.visibility = View.VISIBLE
                unsetStarred?.visibility = View.GONE
            }
        }

        unsetStarred?.setOnClickListener {
            SetUpUnsetStarredAsync().execute()
        }
    }

    private fun getTarget(view: ImageView, fileName: String): com.squareup.picasso.Target {
        return object : com.squareup.picasso.Target {
            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            }

            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                //TODO Acá verificar que haya permisos antes de guardar en almacenamiento externo
                if (Permissions.hasPermissions(context as Activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ExternalStorage.saveFile(bitmap, fileName)
                }
                //Ejemplos de como guardar en cada tipo de storage
                //ExternalStorage.saveFileInCache(activity, bitmap, fileName)
                //InternalStorage.saveFile(context, bitmap, fileName)
                //InternalStorage.saveFileInCache(context, bitmap, fileName)

                view.setImageBitmap(bitmap)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable){
            }
        }
    }
}