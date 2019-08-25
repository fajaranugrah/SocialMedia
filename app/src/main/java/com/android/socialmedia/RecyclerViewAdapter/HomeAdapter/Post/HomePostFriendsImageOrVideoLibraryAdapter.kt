package com.android.socialmedia.RecyclerViewAdapter.HomeAdapter.Post

import android.app.Activity
import android.net.Uri
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder
import com.allattentionhere.autoplayvideos.AAH_VideosAdapter
import com.android.socialmedia.Model.home.Home
import com.android.socialmedia.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.post_friends_libray_image_or_video.view.*
import java.lang.Exception

class HomePostFriendsImageOrVideoLibraryAdapter(val mContext: Activity?, var items : MutableList<Home.PostUser>) : AAH_VideosAdapter() {

    val VIEW_AVATAR : Int = 0

    override fun getItemCount(): Int {
        //items.size
        return items.size
    }

    override fun getItemViewType(position: Int) : Int {
        return VIEW_AVATAR
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): AAH_CustomViewHolder {
        val vh: AAH_CustomViewHolder

        val v = LayoutInflater.from(mContext).inflate(R.layout.post_friends_libray_image_or_video, viewGroup, false)
        vh = ItemRowHolder(v, mContext)

        return vh
    }

    override fun onBindViewHolder(holder: AAH_CustomViewHolder, position : Int) {
        if (items[position].content_type.equals("image")){
            holder?.itemView?.video_sound?.visibility = View.GONE
            holder?.itemView?.post_video_friends?.visibility = View.GONE
            holder?.itemView?.post_image_friends.visibility = View.VISIBLE
            //show image
            Picasso.get()
                .load(items[position].media_filename)
                .centerCrop()
                .fit()
                //.into(holder?.itemView?.post_image_friends.imageView)
                .into(holder?.itemView?.post_image_friends.imageView, object: com.squareup.picasso.Callback {
                    override fun onError(e: Exception?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onSuccess() {
                        //set animations here

                    }
                })
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
            holder?.itemView?.post_video_friends?.isLooping = true

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

    class ItemRowHolder(view: View, mContext: Activity?) : AAH_CustomViewHolder(view) {
        var soundVideo : ImageView = view.video_sound
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

        //override this method to get callback when video starts to play
        override fun videoStarted() {
            super.videoStarted()
            if (isMuted) {
                muteVideo()
                soundVideo.setImageResource(R.drawable.off_sound)
            } else {
                unmuteVideo()
                soundVideo.setImageResource(R.drawable.on_sound)
            }
        }

        override fun pauseVideo() {
            super.pauseVideo()
        }
    }
}