package com.android.socialmedia.RecyclerViewAdapter.HomeAdapter.Post

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.socialmedia.R
import kotlinx.android.synthetic.main.post_story_friends.view.*
import android.text.Html
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.socialmedia.Model.home.Home
import com.android.socialmedia.PicassoKotlin.CircleTransform
import com.squareup.picasso.Picasso


class HomePostFriendsAdapter(val mContext: Activity?, var items : MutableList<Home.Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_AVATAR : Int = 0
    lateinit var adapterPostImage : HomePostFriendsImageAdapter
    //lateinit var adapterPostImageImageOrVideoLibrary : HomePostFriendsImageOrVideoLibraryAdapter

    override fun getItemCount(): Int {
        //items.size
        return items.size
    }

    override fun getItemViewType(position: Int) : Int {
        return VIEW_AVATAR
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder

        val v = LayoutInflater.from(mContext).inflate(R.layout.post_story_friends, viewGroup, false)
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
            .into(holder?.itemView?.icon_friends_post)

        val name : String? = items[position].userName
        holder?.itemView?.username_post.setText(name)

        holder?.itemView?.option_post.setOnClickListener {
            //what did you think on menu post
        }

        //add image post
        adapterPostImage = HomePostFriendsImageAdapter(mContext, items[position].imageOrVideoPostUser)
        //adapterPostImageImageOrVideoLibrary = HomePostFriendsImageOrVideoLibraryAdapter(mContext, items[position].imageOrVideoPostUser)
        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(mContext).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        holder?.itemView?.recycler_view_image_post?.apply {
            //adapter = adapterPostImageImageOrVideoLibrary
            adapter = adapterPostImage
            layoutManager = linearLayoutManager
        }
        LinearSnapHelper().attachToRecyclerView(holder?.itemView?.recycler_view_image_post as RecyclerView?)

        //add indicator page
        if (items[position].imageOrVideoPostUser.size == 1){
            holder?.itemView?.recyclerview_pager_indicator.visibility = View.GONE
        } else{
            holder?.itemView?.recyclerview_pager_indicator.visibility = View.VISIBLE
            holder?.itemView?.recyclerview_pager_indicator.attachToRecyclerView(holder?.itemView?.recycler_view_image_post as RecyclerView?)
        }

        holder?.itemView?.like_post.setOnClickListener {
            //what did you think on like post
        }
        holder?.itemView?.comment_post.setOnClickListener {
            //what did you think on comment post
        }

        val countLike : String? = items[position].likesPost
        val count : String? = "$countLike likes"
        holder?.itemView?.like_count_post.setText(count)

        val username : String? = items[position].userName
        val caption : String? = items[position].captionPostUser
        val captionResult = "<b>$username</b> $caption"
        holder?.itemView?.caption_post.setText(Html.fromHtml(captionResult))
    }

    class ItemRowHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.icon_friends_post
            view.username_post
            view.option_post
            view.recycler_view_image_post
            view.like_post
            view.comment_post
            view.like_count_post
            view.caption_post
        }

    }

}