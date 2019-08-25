package com.android.socialmedia.RecyclerViewAdapter.HomeAdapter.Post

import android.app.Activity
import android.net.Uri
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.socialmedia.Model.home.Home
import com.android.socialmedia.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.post_friends_image.view.*

class HomePostFriendsImageAdapter(val mContext: Activity?, var items : MutableList<Home.PostUser>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_AVATAR : Int = 0

    override fun getItemCount(): Int {
        //items.size
        return items.size
    }

    override fun getItemViewType(position: Int) : Int {
        return VIEW_AVATAR
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder

        val v = LayoutInflater.from(mContext).inflate(R.layout.post_friends_image, viewGroup, false)
        vh = ItemRowHolder(v, mContext)

        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position : Int) {
        if (items[position].content_type.equals("image")){
            holder?.itemView?.video_sound?.visibility = View.GONE
            holder?.itemView?.post_video_friends?.visibility = View.GONE
            holder?.itemView?.post_image_friends.visibility = View.VISIBLE
            //show image
            Picasso.get()
                .load(items[position].media_filename)
                .centerCrop()
                .fit()
                .into(holder?.itemView?.post_image_friends)
        } else if (items[position].content_type.equals("video")){
            holder?.itemView?.video_sound?.visibility = View.VISIBLE
            holder?.itemView?.post_video_friends?.visibility = View.VISIBLE
            holder?.itemView?.post_image_friends.visibility = View.GONE
            //show video
            var myUri : Uri = Uri.parse(items[position].media_filename)
            holder?.itemView?.post_video_friends?.source = myUri
            holder?.itemView?.post_video_friends?.set_act(mContext)
            holder?.itemView?.post_video_friends?.isPaused = false
            holder?.itemView?.post_video_friends?.startVideo()

            holder?.itemView?.post_video_friends?.setOnClickListener {
                if ((holder as ItemRowHolder)?.isMuted) {
                    holder?.itemView?.post_video_friends?.unmuteVideo()
                    holder?.itemView?.video_sound.setImageResource(R.drawable.on_sound)
                } else {
                    holder?.itemView?.post_video_friends?.muteVideo()
                    holder?.itemView?.video_sound.setImageResource(R.drawable.off_sound)
                }
                //for add condition ismuted false or true
                (holder as ItemRowHolder).isMuted = !(holder as ItemRowHolder).isMuted
            }
        }
    }

    class ItemRowHolder(view: View, mContext: Activity?) : RecyclerView.ViewHolder(view) {
        var isMuted : Boolean = false
        init {
            view.post_image_friends

            // This code is used to get the screen dimensions of the user's device
            val displayMetrics = DisplayMetrics()
            mContext?.getWindowManager()?.getDefaultDisplay()?.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels

            view.layoutParams.width = width
            view.layoutParams.height = width
        }
    }
}