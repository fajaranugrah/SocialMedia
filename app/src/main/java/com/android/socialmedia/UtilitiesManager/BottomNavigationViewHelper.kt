package com.android.socialmedia.UtilitiesManager

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.android.socialmedia.NavigationBottom.Activity.ActivityUser
import com.android.socialmedia.NavigationBottom.Home.HomeFragment
import com.android.socialmedia.NavigationBottom.Like.ActivityLike
import com.android.socialmedia.NavigationBottom.Profile.ProfileFragment
import com.android.socialmedia.NavigationBottom.Search.SearchFragment
import com.android.socialmedia.R
import com.google.android.material.bottomnavigation.BottomNavigationView


object BottomNavigationViewHelper {

    private val TAG = "BottomNavigationViewHel"

    fun setupBottomNavigationView(bottomNavigationView: BottomNavigationView) {
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView")
    }

    fun enableNavigation(activity: Activity?, supportFragmentManager: FragmentManager?, view: BottomNavigationView, id: Int) {
        view.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val fragment = HomeFragment()
                    addFragment(fragment, supportFragmentManager, id)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    val fragment = SearchFragment()
                    addFragment(fragment, supportFragmentManager, id)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_activity -> {
                    /*val fragment = ActivityFragment()
                    addFragment(fragment, supportFragmentManager, id)*/

                    //actvity
                    val intent = Intent(activity, ActivityUser::class.java)//ACTIVITY_NUM = 0
                    activity?.startActivity(intent)
                    activity?.overridePendingTransition(0,0)
                    return@OnNavigationItemSelectedListener false
                }
                R.id.navigation_like -> {
                    val fragment = ActivityLike()
                    addFragment(fragment, supportFragmentManager, id)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    val fragment = ProfileFragment()
                    addFragment(fragment, supportFragmentManager, id)
                    return@OnNavigationItemSelectedListener true
                }
            }


            false
        })
    }

    fun addFragment(fragment: Fragment, supportFragmentManager: FragmentManager?, id: Int) {
        supportFragmentManager
            ?.beginTransaction()
            ?.replace(id, fragment, fragment.javaClass.getSimpleName())
            ?.addToBackStack(fragment.javaClass.getSimpleName())
            ?.commit()
    }
}