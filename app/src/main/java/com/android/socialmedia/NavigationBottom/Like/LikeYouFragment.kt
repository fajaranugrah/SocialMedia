package com.android.socialmedia.NavigationBottom.Like

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.socialmedia.Model.like.Like
import com.android.socialmedia.Pagination.PaginationScrollListener
import com.android.socialmedia.RecyclerViewAdapter.LikeAdapter.LikeYouAdapter.LikeYouAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.like_you_fragment.*
import com.android.socialmedia.R

class LikeYouFragment : Fragment(){

    var activityLike : Activity = Activity()
    var fragmentActivity : FragmentActivity = FragmentActivity()
    val ACTIVITY_NUM : Int = 3

    lateinit var adapterLikeYou : LikeYouAdapter
    var isLastPage: Boolean = false
    var isLoading: Boolean = false

    //for like you
    var likeYou : MutableList<Like.You> = mutableListOf()

    private lateinit var database: DatabaseReference

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activityLike = activity
        fragmentActivity = activity as FragmentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.like_you_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Write a message to the database
        database = FirebaseDatabase.getInstance().reference
        database.keepSynced(true)

        //for data like you
        val likeYouListener = object : ValueEventListener {
            @SuppressLint("WrongConstant")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                likeYou.clear()
                dataSnapshot.children.mapNotNullTo(likeYou) {
                    it.getValue<Like.You>(Like.You::class.java)
                }
                likeYou.forEach {
                        storyCheck -> Log.i("checkStory", "$storyCheck")
                }

                adapterLikeYou = LikeYouAdapter(activityLike, likeYou)
                val linearLayoutManagerStory : LinearLayoutManager = LinearLayoutManager(activityLike).apply {
                    orientation = LinearLayoutManager.VERTICAL
                }
                list_like_you.apply {
                    adapter = adapterLikeYou
                    layoutManager = linearLayoutManagerStory
                    addOnScrollListener(object : PaginationScrollListener(linearLayoutManagerStory) {
                        override fun isLastPage(): Boolean {
                            return isLastPage
                        }

                        override fun isLoading(): Boolean {
                            return isLoading
                        }

                        override fun loadMoreItems() {
                            isLoading = true
                            //you have to call loadmore items to get more data
                            //getMoreItems()
                            Log.e("checkLoadMore", "yeah load more story")
                        }
                    })
                }

                swipeContainerLikeYou.isRefreshing = false
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("CheckDatabaseStory", "loadPost:onCancelled", databaseError.toException())
            }
        }
        //add calling database firebase
        database.child("likeYou").addListenerForSingleValueEvent(likeYouListener)

        //refresh swipe
        swipeContainerLikeYou.setOnRefreshListener {
            swipeContainerLikeYou.isRefreshing = true
            database.child("likeYou").addListenerForSingleValueEvent(likeYouListener)
        }

    }
}