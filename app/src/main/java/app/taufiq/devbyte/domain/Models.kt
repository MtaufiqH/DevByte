package app.taufiq.devbyte.domain

import app.taufiq.devbyte.util.smartTruncate

/**
 * Created By Taufiq on 8/18/2020.
 *
 */


data class DevbyteVideos(
    val title: String,
    val description: String,
    val url: String,
    val updated: String,
    val thumbnail: String
) {

    val shortDescription: String
    get() =  description.smartTruncate(200)

}