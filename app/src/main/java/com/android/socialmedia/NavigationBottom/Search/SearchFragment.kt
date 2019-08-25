package com.android.socialmedia.NavigationBottom.Search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.socialmedia.Model.search.Search
import com.android.socialmedia.Pagination.PaginationScrollListenerStaggered
import com.android.socialmedia.RecyclerViewAdapter.SearchAdapter.SearchAdapter
import com.android.socialmedia.UtilitiesManager.BottomNavigationViewHelper
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.layout_bottombar_navigation_view.*
import kotlinx.android.synthetic.main.search_fragment.*
import com.android.socialmedia.R

class SearchFragment : Fragment(){

    var activitySearch : Activity? = Activity()
    var fragmentActivity : FragmentActivity = FragmentActivity()
    val ACTIVITY_NUM : Int = 1
    lateinit var adapterSearch : SearchAdapter

    var isLastPage: Boolean = false
    var isLoading: Boolean = false

    private lateinit var database: DatabaseReference

    //for post friends
    var post : MutableList<Search.Post> = mutableListOf()

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activitySearch = activity
        fragmentActivity = activity as FragmentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.search_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomNavigationView()

        toolbar_search.setOnClickListener {
            val i = Intent(activitySearch, SearchUserActivity::class.java)
            activitySearch?.startActivity(i)
        }

        // Write a message to the database
        database = FirebaseDatabase.getInstance().reference
        database.keepSynced(true)
        //for post friends
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                post.clear()
                dataSnapshot.children.mapNotNullTo(post) {
                    it.getValue<Search.Post>(Search.Post::class.java)
                }
                post.forEach {
                        storyCheck -> Log.i("checkStory", "$storyCheck")
                }

                //for post friends
                adapterSearch = SearchAdapter(activitySearch, post)
                val staggeredGridLayoutManager : StaggeredGridLayoutManager = StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL)
                search?.apply {
                    adapter = adapterSearch

                    layoutManager = staggeredGridLayoutManager
                    isNestedScrollingEnabled
                    addOnScrollListener(object : PaginationScrollListenerStaggered(staggeredGridLayoutManager, post.size) {
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

                swipeContainerSearch.isRefreshing = false
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("CheckDatabaseStory", "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.child("search").addListenerForSingleValueEvent(postListener)

        //refresh swipe
        swipeContainerSearch.setOnRefreshListener {
            swipeContainerSearch.isRefreshing = true
            database.child("search").addListenerForSingleValueEvent(postListener)
        }
    }

    fun setupBottomNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(navigationView)
        BottomNavigationViewHelper.enableNavigation(activitySearch, fragmentActivity.supportFragmentManager, navigationView, content.id)
        val menu : Menu = navigationView.menu
        val menuItem : MenuItem = menu.getItem(ACTIVITY_NUM)
        menuItem.isChecked = true
    }
}