package com.android.socialmedia.RecyclerViewAdapter.LikeAdapter.LikeFollowAdapter

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
import kotlinx.android.synthetic.main.like_follow.view.*

class LikeFollowAdapter(val mContext: Activity, var items : MutableList<Like.Follow>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_AVATAR : Int = 0

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int) : Int {
        return VIEW_AVATAR
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder

        val v = LayoutInflater.from(mContext).inflate(R.layout.like_follow, viewGroup, false)
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
            .into(holder?.itemView?.icon_friends_like_follow)

        val usernameLike : String? = items[position].userName
        val mentionUser : String? = items[position].mentionUser
        if (mentionUser.equals("")){
            holder?.itemView?.message_like_follow.setText(usernameLike)
        } else {
            val showLike : String? = "<b>$usernameLike</b> liked <b>$mentionUser</b> post."
            holder?.itemView?.message_like_follow.setText(Html.fromHtml(showLike))
        }

        holder?.itemView?.like_follow_row_bg.setOnClickListener {
            //what did you think on open this
        }

        Glide.with(mContext)
            .load(items[position].imageLike)
            .into(holder?.itemView?.picture_like_follow)
    }

    class ItemRowHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.icon_friends_like_follow
            view.message_like_follow
            view.picture_like_follow
        }

    }

}