package com.android.socialmedia.NavigationBottom.Search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.socialmedia.ListAdapter.ListSearchAdapter
import com.android.socialmedia.Model.search.Search
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.search_user.*
import kotlinx.android.synthetic.main.search_user_layout.*
import com.android.socialmedia.R

class SearchUserActivity : AppCompatActivity() {

    lateinit var adapterSearch : ListSearchAdapter

    private lateinit var database: DatabaseReference

    //for post friends
    var post : MutableList<Search.Post> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_user_layout)

        btn_clear_search.visibility = View.GONE
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
                adapterSearch = ListSearchAdapter(applicationContext, R.layout.item_search_selection, post)
                list_search?.apply {
                    adapter = adapterSearch

                    isNestedScrollingEnabled

                    search.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                        }

                        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                            if (s.toString() != "") {
                                btn_clear_search.visibility = View.VISIBLE
                                adapterSearch.getFilter().filter(s.toString())
                            } else {
                                btn_clear_search.visibility = View.GONE
                                adapterSearch.getFilter().filter(s.toString())
                            }
                        }

                        override fun afterTextChanged(s: Editable) {

                        }
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("CheckDatabaseStory", "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.child("search").addListenerForSingleValueEvent(postListener)

        btn_clear_search.setOnClickListener {
            search.text = null
            search.clearFocus()
        }

        back_arrow.setOnClickListener {
            onBackPressed()
        }
    }

}