package com.android.socialmedia.RecyclerViewAdapter.album

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.socialmedia.R
import kotlinx.android.synthetic.main.list_single_album_layout.view.*

abstract class SingleAlbumAdapter(val albumList: MutableList<String>, val context: Context, val size: RequestOptions,
                                  val glide: RequestBuilder<Bitmap>, val glideMain: RequestManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_AVATAR : Int = 0

    override fun getItemViewType(position: Int) : Int {
        return VIEW_AVATAR
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    abstract fun selectedImage(position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vh: RecyclerView.ViewHolder

        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_single_album_layout, parent, false)
        vh = ViewHolder(v)

        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position : Int) {
        Log.e("ClickSingleAlbum", albumList.get(position))
        glide.load(albumList.get(position))
            .apply(size)
            .centerCrop()
            .into(holder.itemView.image_gallery)

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                selectedImage(position)
            }
        })
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.image_gallery
        }
    }
}
