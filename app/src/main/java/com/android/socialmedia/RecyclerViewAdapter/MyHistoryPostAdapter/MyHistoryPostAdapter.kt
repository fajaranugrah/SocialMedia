package com.android.socialmedia.RecyclerViewAdapter.MyHistoryPostAdapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.socialmedia.Model.profile.Profile
import com.android.socialmedia.R
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.list_my_history_post_layout.view.*

class MyHistoryPostAdapter(val myHistoryPost: MutableList<Profile.MyPost>?, val context: Context, val size: RequestOptions,
                           val glide: RequestBuilder<Bitmap>, val glideMain: RequestManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_AVATAR : Int = 0

    override fun getItemViewType(position: Int) : Int {
        return VIEW_AVATAR
    }

    override fun getItemCount(): Int {
        return myHistoryPost!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vh: RecyclerView.ViewHolder

        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_my_history_post_layout, parent, false)
        vh = ViewHolder(v)

        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position : Int) {
        Log.e("ClickMyHistoryPost", myHistoryPost?.get(position)?.media_filename)
        glide.load(myHistoryPost?.get(position)?.media_filename)
            .apply(size)
            .centerCrop()
            .into(holder.itemView.image_my_post)

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                //click history post
            }
        })
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.image_my_post
        }
    }
}