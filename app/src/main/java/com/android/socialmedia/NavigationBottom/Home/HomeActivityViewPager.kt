package com.android.socialmedia.NavigationBottom.Home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.android.socialmedia.R
import com.android.socialmedia.UtilitiesManager.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_home_view_pager.*
import kotlinx.android.synthetic.main.layout_bottombar_navigation_view.*

class HomeActivityViewPager : AppCompatActivity() {

    val ACTIVITY_NUM : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_view_pager)

        setupBottomNavigationView()
    }

    fun setupBottomNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(navigationView)
        BottomNavigationViewHelper.enableNavigation(this, supportFragmentManager, navigationView, content.id)
        val menu : Menu = navigationView.menu
        val menuItem : MenuItem = menu.getItem(ACTIVITY_NUM)
        menuItem.isChecked = true
    }
}