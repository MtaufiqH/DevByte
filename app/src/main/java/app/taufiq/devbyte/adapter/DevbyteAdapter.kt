package app.taufiq.devbyte.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.taufiq.devbyte.R
import app.taufiq.devbyte.databinding.DevByteItemBinding
import app.taufiq.devbyte.domain.DevbyteVideos

/**
 * Created By Taufiq on 8/21/2020.
 *
 */
class DevbyteAdapter(val callback: VideoClick) :
    RecyclerView.Adapter<DevbyteAdapter.DevbyteViewHolder>() {

    var videos: List<DevbyteVideos> = emptyList()
        set(value) {
            field = value

            notifyDataSetChanged()
        }

    class DevbyteViewHolder(
        val devByteItemBinding: DevByteItemBinding): RecyclerView.ViewHolder(devByteItemBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.dev_byte_item
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevbyteViewHolder {
        val withDatabinding:DevByteItemBinding =  DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            DevbyteViewHolder.LAYOUT, parent, false
        )
        return DevbyteViewHolder(withDatabinding)
    }

    override fun getItemCount() = videos.size

    override fun onBindViewHolder(holder: DevbyteViewHolder, position: Int) {
        holder.devByteItemBinding.also {
            it.video = videos[position]
            it.videoCallback  = callback
        }
    }

}


/**
 * Click listener for Videos. By giving the block a name it helps a reader understand what it does.
 *
 */
class VideoClick(val block: (DevbyteVideos) -> Unit) {
    /**
     * Called when a video is clicked
     *
     * @param video the video that was clicked
     */
    fun onClick(video: DevbyteVideos) = block(video)
}