package com.android.socialmedia.NavigationBottom.Like

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.android.socialmedia.PagerTransformer.SectionsPagerAdapter
import com.android.socialmedia.R
import com.android.socialmedia.UtilitiesManager.BottomNavigationViewHelper
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_like.*
import kotlinx.android.synthetic.main.layout_bottombar_navigation_view.*

class ActivityLike : Fragment() {

    var activityLike : Activity? = Activity()
    var fragmentActivity : FragmentActivity = FragmentActivity()
    var tabLayout_like : TabLayout? = null
    var viewPager_like : ViewPager? = null
    var pagerAdapter : SectionsPagerAdapter? = null
    val ACTIVITY_NUM : Int = 3

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activityLike = activity
        fragmentActivity = activity as FragmentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.activity_like, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomNavigationView()

        tabLayout_like = view.findViewById<TabLayout>(R.id.tabLayout_like)
        viewPager_like = view.findViewById<ViewPager>(R.id.viewPager_like)

        tabLayout_like!!.addTab(tabLayout_like!!.newTab().setText("Follow"))
        tabLayout_like!!.addTab(tabLayout_like!!.newTab().setText("You"))
        tabLayout_like!!.tabGravity = TabLayout.GRAVITY_FILL

        pagerAdapter = SectionsPagerAdapter(childFragmentManager)
        pagerAdapter!!.addFragment(LikeFollowFragment())
        pagerAdapter!!.addFragment(LikeYouFragment())

        viewPager_like!!.adapter = pagerAdapter

        viewPager_like!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout_like))
        tabLayout_like!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager_like!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    fun setupBottomNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(navigationView)
        BottomNavigationViewHelper.enableNavigation(activityLike, fragmentActivity.supportFragmentManager, navigationView, content.id)
        val menu : Menu = navigationView.menu
        val menuItem : MenuItem = menu.getItem(ACTIVITY_NUM)
        menuItem.isChecked = true
    }
}