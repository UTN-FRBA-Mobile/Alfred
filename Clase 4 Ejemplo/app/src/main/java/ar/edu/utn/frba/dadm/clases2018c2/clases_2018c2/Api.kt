package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

//https://medium.com/@elye.project/kotlin-and-retrofit-2-tutorial-with-working-codes-333a4422a890
interface Api{
    @GET("list")
    fun listRepos(): Observable<Response>

    companion object {
        fun create(): Api{
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .baseUrl("https://demo0682762.mockable.io/")
                    .build()

            return retrofit.create(Api::class.java)
        }
    }
}