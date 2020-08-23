package app.taufiq.devbyte.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.taufiq.devbyte.database.getDatabase
import app.taufiq.devbyte.repository.VideoRepository
import retrofit2.HttpException
import timber.log.Timber

/**
 * Created By Taufiq on 8/23/2020.
 *
 */
class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object{
        const val WORK_NAME = "app.taufiq.devbyte.work.RefreshDataWorker"
    }


    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = VideoRepository(database)

        try {
            repository.refreshVideo()
            Timber.d("Work request for sync is run")
        } catch (e: HttpException) {
            /**To resolve the "Unresolved reference" error, import retrofit2.HttpException.*/
            return Result.retry()
        }

        return Result.success()
    }
}