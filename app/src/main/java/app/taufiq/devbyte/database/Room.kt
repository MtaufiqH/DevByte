package app.taufiq.devbyte.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created By Taufiq on 8/18/2020.
 *
 */

@Dao
interface VideoDao {
    @Query("SELECT * FROM DatabaseVideo")
    fun getVideo(): LiveData<List<DatabaseVideo>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(video: List<DatabaseVideo>)
}


@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideoDatabase : RoomDatabase() {

    abstract val videoDao: VideoDao

}

private lateinit var INSTANCE: VideoDatabase


fun getDatabase(context: Context): VideoDatabase {
    synchronized(VideoDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                VideoDatabase::class.java,
                "Videos"
            ).build()
        }
    }

    return INSTANCE
}