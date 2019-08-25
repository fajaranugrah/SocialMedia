package com.android.socialmedia.PagerTransformer

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

/**
 * Class that stores fragments for tabs
 */
class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mActivityList = ArrayList<AppCompatActivity>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun getItemActivity(position: Int): AppCompatActivity {
        return mActivityList[position]
    }


    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun getCountActivity(): Int {
        return mActivityList.size
    }

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    fun addActivity(appCompatActivity: AppCompatActivity) {
        mActivityList.add(appCompatActivity)
    }

    companion object {

        private val TAG = "SectionsPagerAdapter"
    }

}