package com.android.socialmedia.RecyclerViewAdapter.HomeAdapter.Story

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.socialmedia.Model.home.Home
import com.android.socialmedia.NavigationBottom.Stories.Stories
import com.android.socialmedia.PicassoKotlin.CircleTransform
import com.android.socialmedia.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.story_friends.view.*

class HomeStoryFriendsAdapter(val mContext: Activity?, var items : MutableList<Home.Story>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG : String = "Stories"
    val VIEW_AVATAR : Int = 0
    var mViewHolders : HashMap<Int, RecyclerView.ViewHolder> = HashMap()
    val CLICK_DURATION : Int = 500
    var mOnTouchRunnable : Runnable = Runnable {  }
    var isRunning : Boolean = false
    var down : Boolean = false
    var up : Boolean = false
    var x1 : Float = 0.0F
    var y1 : Float = 0.0F
    var x2 : Float = 0.0F
    var y2 : Float = 0.0F
    var runningTime : Long = 0

    override fun getItemCount(): Int {
        //items.size
        return items.size
    }

    override fun getItemViewType(position: Int) : Int {
        return VIEW_AVATAR
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder

        val v = LayoutInflater.from(mContext).inflate(R.layout.story_friends, viewGroup, false)
        vh = ItemRowHolder(v)

        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position : Int) {

        //your Story
        Log.e("checkArray", items[position].userAvatar)
        val circle : CircleTransform = CircleTransform()
        Picasso.get()
            .load(items[position].userAvatar)
            .centerCrop()
            .fit()
            .transform(circle)
            .into(holder?.itemView?.icon_friends)

        if (position==0){
            holder?.itemView?.your_account.visibility
        } else{
            holder?.itemView?.your_account.visibility = View.GONE
        }
        holder?.itemView?.name_story.text = items[position].userName

        //numStories = items[position].userStories
        if (items[position].userStories.toInt()==0){
            holder?.itemView?.touch_image_stories.background = ContextCompat.getDrawable(mContext!!.applicationContext, R.drawable.circle_white)
        } else {
            holder?.itemView?.touch_image_stories.background = ContextCompat.getDrawable(mContext!!.applicationContext, R.drawable.circle_light_blue)
        }

        mViewHolders.put(position, holder)

        holder?.itemView?.touch_image_stories.setOnClickListener {
            val i = Intent(mContext, Stories::class.java)
            i.putExtra("StoriesUser", items[position].videoLink)
            mContext.startActivity(i)
        }

    }

    fun startProgressBar() {
        mViewHolders.get(0)?.itemView?.story_progress_bar?.visibility = View.VISIBLE
    }

    fun stopProgressBar() {
        mViewHolders.get(0)?.itemView?.story_progress_bar?.visibility = View.GONE
        mViewHolders.get(0)?.itemView?.touch_image_stories?.background = ContextCompat.getDrawable(mContext!!.applicationContext, R.drawable.circle_white)
    }

    inner class ItemRowHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.icon_friends
        }

    }

}