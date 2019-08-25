package com.android.socialmedia.NavigationBottom.Profile

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.socialmedia.Model.profile.Profile
import com.android.socialmedia.PicassoKotlin.CircleTransform
import com.android.socialmedia.RecyclerViewAdapter.MyHistoryPostAdapter.MyHistoryPostAdapter
import com.android.socialmedia.UtilitiesManager.BottomNavigationViewHelper
import com.android.socialmedia.UtilitiesManager.BottomSheet.BottomSheet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_bottombar_navigation_view.*
import kotlinx.android.synthetic.main.profile_fragment.*
import com.android.socialmedia.R

class ProfileFragment : Fragment(){

    var activityProfile : Activity = Activity()
    var fragmentActivity : FragmentActivity = FragmentActivity()
    val ACTIVITY_NUM : Int = 4
    lateinit var adapterHistoryPost : MyHistoryPostAdapter

    var accountInfo : Profile.AccountInfo? = Profile.AccountInfo()

    private lateinit var database: DatabaseReference

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activityProfile = activity
        fragmentActivity = activity as FragmentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.profile_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigationView()

        val displayMetrics = DisplayMetrics()
        activityProfile.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        setting_profile.setOnClickListener {
            val sheet = BottomSheet()
            sheet.show(fragmentActivity.supportFragmentManager, "DemoBottomSheetFragment")
        }

        list_history_post_profile.layoutManager = GridLayoutManager(activityProfile, 4)
        list_history_post_profile?.setHasFixedSize(true)

        // Write a message to the database
        database = FirebaseDatabase.getInstance().reference
        database.keepSynced(true)
        //my history post
        val myHistoryListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                accountInfo = dataSnapshot.getValue(Profile.AccountInfo::class.java)

                val options = RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).override(width/ 4, width / 4).skipMemoryCache(true).error(R.drawable.ic_launcher_background)
                val glide = Glide.with(activityProfile)
                val builder = glide.asBitmap()

                Log.e("checkImageSize", accountInfo?.imageOrVideoPostUser?.size.toString())

                adapterHistoryPost = MyHistoryPostAdapter(accountInfo?.imageOrVideoPostUser, activityProfile, options, builder, glide)
                list_history_post_profile?.adapter = adapterHistoryPost

                list_history_post_profile?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                    }

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        when (newState) {
                            RecyclerView.SCROLL_STATE_IDLE -> glide.resumeRequests()
                            AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL, AbsListView.OnScrollListener.SCROLL_STATE_FLING -> glide.pauseRequests()
                        }
                    }
                }
                )

                //title profile
                title_account.text = accountInfo?.userName
                username_profile.text = accountInfo?.userName

                //description
                description_profile.text = accountInfo?.messageProfile

                //avatar account
                val circle : CircleTransform = CircleTransform()
                Picasso.get()
                    .load(accountInfo?.userAvatar)
                    .centerCrop()
                    .fit()
                    .transform(circle)
                    .into(avatar_ptofile)

                //value post
                value_post_profile.text = accountInfo?.numberPost.toString()

                //value followers
                value_followers_profile.text = accountInfo?.numberFollowers.toString()

                //value following
                value_following_profile.text = accountInfo?.numberFollowing.toString()

                swipeContainerProfile.isRefreshing = false
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("CheckDatabaseStory", "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.child("accountInfo").addListenerForSingleValueEvent(myHistoryListener)

        //refresh swipe
        swipeContainerProfile.setOnRefreshListener {
            swipeContainerProfile.isRefreshing = true
            database.child("accountInfo").addListenerForSingleValueEvent(myHistoryListener)
        }
    }

    fun setupBottomNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(navigationView)
        BottomNavigationViewHelper.enableNavigation(activityProfile, fragmentActivity.supportFragmentManager, navigationView, content.id)
        val menu : Menu = navigationView.menu
        val menuItem : MenuItem = menu.getItem(ACTIVITY_NUM)
        menuItem.isChecked = true
    }
}