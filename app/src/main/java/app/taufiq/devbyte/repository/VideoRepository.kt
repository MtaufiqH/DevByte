package app.taufiq.devbyte.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import app.taufiq.devbyte.database.VideoDatabase
import app.taufiq.devbyte.database.asDomainModel
import app.taufiq.devbyte.domain.DevbyteVideos
import app.taufiq.devbyte.network.DevByteNetwork
import app.taufiq.devbyte.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created By Taufiq on 8/18/2020.
 *
 */


/**
 * Repository for fetching devbyte videos from the network and storing them on disk
 */
class VideoRepository(private val database: VideoDatabase) {

    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     */
    suspend fun refreshVideo() {
        withContext(Dispatchers.IO) {
            val playlist = DevByteNetwork.devbytes.getPlaylist().await()

            database.videoDao.insertAll(playlist.asDatabaseModel())
        }
    }

    val videos: LiveData<List<DevbyteVideos>> = Transformations.map(database.videoDao.getVideo()){
        it.asDomainModel()
    }
}