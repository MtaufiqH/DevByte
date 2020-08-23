package app.taufiq.devbyte.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.taufiq.devbyte.domain.DevbyteVideos

/**
 * Created By Taufiq on 8/18/2020.
 *
 */

/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */
@Entity
data class DatabaseVideo constructor(
    @PrimaryKey
    val url: String,
    val updated: String,
    val title: String,
    val description: String,
    val thumbnail: String
)

/**
 * Map DatabaseVideos to domain entities
 */
fun List<DatabaseVideo>.asDomainModel(): List<DevbyteVideos> {
    return map {
        DevbyteVideos(
            url = it.url,
            thumbnail = it.thumbnail,
            updated = it.updated,
            description = it.description,
            title = it.title)
    }
}