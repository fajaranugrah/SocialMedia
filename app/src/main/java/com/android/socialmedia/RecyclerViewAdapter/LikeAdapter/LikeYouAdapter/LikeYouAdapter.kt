package com.android.socialmedia.RecyclerViewAdapter.LikeAdapter.LikeYouAdapter

import android.app.Activity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.socialmedia.Model.like.Like
import com.android.socialmedia.PicassoKotlin.CircleTransform
import com.android.socialmedia.R
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.like_you.view.*

class LikeYouAdapter(val mContext: Activity, var items : MutableList<Like.You>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_AVATAR : Int = 0

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int) : Int {
        return VIEW_AVATAR
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder

        val v = LayoutInflater.from(mContext).inflate(R.layout.like_you, viewGroup, false)
        vh = ItemRowHolder(v)

        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position : Int) {

        val circle : CircleTransform = CircleTransform()
        Picasso.get()
            .load(items[position].userAvatar)
            .centerCrop()
            .fit()
            .transform(circle)
            .into(holder?.itemView?.icon_friends_like_you)

        val usernameLike : String? = items[position].userName
        val message : String? = items[position].message
        val mentionUser : String? = items[position].mentionUser
        if (message.equals("")){
            val showLike : String? = "<b>$usernameLike</b> like your post."
            holder?.itemView?.message_like_you.setText(Html.fromHtml(showLike))
        } else {
            val showLike : String? = "<b>$usernameLike</b> mentioned you in a comment: $message  <font color=blue>$mentionUser</font>"
            holder?.itemView?.message_like_you.setText(Html.fromHtml(showLike))
        }

        holder?.itemView?.like_you_row_bg.setOnClickListener {
            //what did you think on open this
        }

        Glide.with(mContext)
            .load(items[position].imageLike)
            .into(holder?.itemView?.picture_like_you)
    }

    class ItemRowHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.icon_friends_like_you
            view.message_like_you
            view.picture_like_you
        }

    }

}