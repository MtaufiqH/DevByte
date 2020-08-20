package app.taufiq.devbyte.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/**
 * Created By Taufiq on 8/18/2020.
 *
 */

private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"

/** A retrofit service to fetch  a devbyte playlist*/
interface DevByteService {
    @GET("devbytes")
    fun getPlaylist(): Deferred<NetworkVideoContainer>

}

/**
 * Main entry point for network access. Call like `DevByteNetwork.devbytes.getPlaylist()`
 */
object DevByteNetwork {

    // configure retrofit to parse json and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val devbytes = retrofit.create(DevByteService::class.java)
}
