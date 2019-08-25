package com.android.socialmedia.NavigationBottom.Home

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.socialmedia.Model.home.Home
import com.android.socialmedia.Pagination.PaginationScrollListener
import com.android.socialmedia.RecyclerViewAdapter.HomeAdapter.Post.HomePostFriendsAdapter
import com.android.socialmedia.RecyclerViewAdapter.HomeAdapter.Story.HomeStoryFriendsAdapter
import com.android.socialmedia.UtilitiesManager.BottomNavigationViewHelper
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.layout_bottombar_navigation_view.*
import com.android.socialmedia.R


class HomeFragment : Fragment() {

    var activityHome : Activity? = Activity()
    var fragmentActivity : FragmentActivity = FragmentActivity()
    lateinit var adapterStory : HomeStoryFriendsAdapter
    lateinit var adapterPost : HomePostFriendsAdapter
    //lateinit var adapterPostLibrary : HomePostFriendsLibraryAdapter
    val ACTIVITY_NUM : Int = 0

    //for story friends
    var story : MutableList<Home.Story> = mutableListOf()
    var myStory : Home.Story = Home.Story()

    //show all story
    var allStory : MutableList<Home.Story> = mutableListOf()

    //for post friends
    var post : MutableList<Home.Post> = mutableListOf()

    var isLastPage: Boolean = false
    var isLoading: Boolean = false

    private lateinit var database: DatabaseReference

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activityHome = activity
        fragmentActivity = activity as FragmentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.home_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomNavigationView()
        val mtoolbar : Toolbar = toolbar
        mtoolbar.apply {
            isOverflowMenuShowing
        }

        //add storage firebase
        val storage : FirebaseStorage = FirebaseStorage.getInstance("gs://project1fajar.appspot.com")
        // Create a storage reference from our app
        val storageRef = storage.reference

        // Get reference to the file
        val forestRef = storageRef.child("images/myProfile.jpg")

        var avatarProfile : Uri = Uri.EMPTY
        forestRef.downloadUrl.addOnSuccessListener {
            // Got the download URL for 'users/me/profile.png'
            avatarProfile = it
            Log.e("CheckStorage", it.toString())
            Log.e("CheckStorage", avatarProfile.toString())
        }.addOnFailureListener {
            // Handle any errors
            Log.e("CheckStorage", it.toString())
        }

        // Write a message to the database
        database = FirebaseDatabase.getInstance().reference
        database.keepSynced(true)
        //my story
        val myStoryListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                allStory.clear()
                val post = dataSnapshot.getValue(myStory::class.java)
                if (post != null) {
                    allStory.add(post)
                }

                //my friend story
                val storyListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // Get Post object and use the values to update the UI
                        story.clear()
                        dataSnapshot.children.mapNotNullTo(story) {
                            it.getValue<Home.Story>(Home.Story::class.java)
                        }
                        story.forEachIndexed {
                                index, storyCheck -> Log.i("checkStory", "$storyCheck")
                            allStory.add(storyCheck)
                        }

                        allStory.forEachIndexed {
                                index, storyCheck -> Log.i("checkAllStory", "$storyCheck")
                        }

                        //for all story friends
                        adapterStory = HomeStoryFriendsAdapter(activityHome, allStory)
                        val linearLayoutManagerStory : LinearLayoutManager = LinearLayoutManager(activityHome).apply {
                            orientation = LinearLayoutManager.HORIZONTAL
                        }
                        story_home.apply {
                            adapter = adapterStory
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
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w("CheckDatabaseStory", "loadPost:onCancelled", databaseError.toException())
                    }
                }
                database.child("story").addListenerForSingleValueEvent(storyListener)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("CheckDatabaseStory", "loadPost:onCancelled", databaseError.toException())
            }
        }
        //add calling database firebase
        database.child("myStory").addListenerForSingleValueEvent(myStoryListener)

        //for post friends
        val postListener = object : ValueEventListener {
            @SuppressLint("WrongConstant")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                post.clear()
                dataSnapshot.children.mapNotNullTo(post) {
                    it.getValue<Home.Post>(Home.Post::class.java)
                }
                post.forEach {
                        storyCheck -> Log.i("checkStory", "$storyCheck")
                }

                //for post friends
                adapterPost = HomePostFriendsAdapter(activityHome, post)
                //adapterPostLibrary = HomePostFriendsLibraryAdapter(activityHome, post)
                val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(activityHome).apply {
                    orientation = LinearLayoutManager.VERTICAL
                }
                post_home?.apply {
                    adapter = adapterPost
                    //adapter = adapterPostLibrary

                    //add for play video
                    /*setActivity(activityHome)
                    itemAnimator = DefaultItemAnimator()
                    setPlayOnlyFirstVideo(true)// false by default
                    setVisiblePercent(50.0F)// percentage of View that needs to be visible to start playing
                    //call this functions when u want to start autoplay on loading async lists (eg firebase)
                    smoothScrollBy(1,0)
                    smoothScrollBy(-1,0)*/

                    layoutManager = linearLayoutManager
                    isNestedScrollingEnabled
                    addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
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

                swipeContainerHome.isRefreshing = false
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("CheckDatabaseStory", "loadPost:onCancelled", databaseError.toException())
            }
        }
        //add calling database firebase
        database.child("post").addListenerForSingleValueEvent(postListener)

        //refresh swipe
        swipeContainerHome.setOnRefreshListener {
            swipeContainerHome.isRefreshing = true
            database.child("myStory").addListenerForSingleValueEvent(myStoryListener)
            database.child("post").addListenerForSingleValueEvent(postListener)
        }

    }

    fun setupBottomNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(navigationView)
        BottomNavigationViewHelper.enableNavigation(activityHome, fragmentActivity.supportFragmentManager, navigationView, content.id)
        val menu : Menu = navigationView.menu
        val menuItem : MenuItem = menu.getItem(ACTIVITY_NUM)
        menuItem.isChecked = true
    }
}