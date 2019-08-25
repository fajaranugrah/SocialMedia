package com.android.socialmedia.RecyclerViewAdapter.SearchAdapter

import android.app.Activity
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.socialmedia.Model.search.Search
import com.android.socialmedia.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search.view.*

class SearchAdapter (val mContext: Activity?, var itemsPost : MutableList<Search.Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_AVATAR : Int = 0

    override fun getItemCount(): Int {
        //items.size
        return itemsPost.size
    }

    override fun getItemViewType(position: Int) : Int {
        return VIEW_AVATAR
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder

        val v = LayoutInflater.from(mContext).inflate(R.layout.search, viewGroup, false)
        vh = ItemRowHolder(v, mContext)

        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position : Int) {
        var items : MutableList<Search.PostUser> = itemsPost[position].imageOrVideoPostUser

        items.forEachIndexed {
                index, storyCheck -> Log.i("checkSearch", "$storyCheck")

            if (storyCheck.id.equals("1")) {
                if (storyCheck.content_type.equals("image")) {
                    holder?.itemView?.video_sound?.visibility = View.GONE
                    holder?.itemView?.search_video?.visibility = View.GONE
                    holder?.itemView?.search_image.visibility = View.VISIBLE
                    //show image
                    Picasso.get()
                        .load(storyCheck.media_filename)
                        .centerCrop()
                        .fit()
                        .into(holder?.itemView?.search_image)
                    return
                } else if (storyCheck.content_type.equals("video")){
                    holder?.itemView?.video_sound?.visibility = View.VISIBLE
                    holder?.itemView?.search_video?.visibility = View.VISIBLE
                    holder?.itemView?.search_image.visibility = View.GONE
                    //show video
                    var myUri : Uri = Uri.parse(storyCheck.media_filename)
                    holder?.itemView?.search_video?.source = myUri
                    holder?.itemView?.search_video?.set_act(mContext)
                    holder?.itemView?.search_video?.isPaused = false
                    holder?.itemView?.search_video?.startVideo()

                    holder?.itemView?.search_video?.setOnClickListener {
                        if ((holder as ItemRowHolder)?.isMuted) {
                            holder?.itemView?.search_video?.unmuteVideo()
                            holder?.itemView?.video_sound.setImageResource(R.drawable.on_sound)
                        } else {
                            holder?.itemView?.search_video?.muteVideo()
                            holder?.itemView?.video_sound.setImageResource(R.drawable.off_sound)
                        }
                        //for add condition ismuted false or true
                        (holder as ItemRowHolder).isMuted = !(holder as ItemRowHolder).isMuted
                    }
                    return
                }
            }
        }
    }

    class ItemRowHolder(view: View, mContext: Activity?) : RecyclerView.ViewHolder(view) {
        var isMuted : Boolean = false
        init {
            view.search_image

            // This code is used to get the screen dimensions of the user's device
            val displayMetrics = DisplayMetrics()
            mContext?.getWindowManager()?.getDefaultDisplay()?.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            val height = displayMetrics.heightPixels

            view.layoutParams.width = width
            view.layoutParams.height = height / 5
        }
    }
}