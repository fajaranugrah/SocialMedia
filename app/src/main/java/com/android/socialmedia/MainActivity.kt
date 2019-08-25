package com.android.socialmedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.android.socialmedia.NavigationBottom.Home.CameraFragment
import com.android.socialmedia.NavigationBottom.Home.HomeFragment
import com.android.socialmedia.PagerTransformer.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val HOME_FRAGMENT : Int = 1
    var doubleBackToExitPressedOnce : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager(supportFragmentManager)
    }

    override fun onStart() {
        super.onStart()
        pager_main.currentItem = HOME_FRAGMENT
    }

    fun viewPager(fm: FragmentManager){
        val pagerAdapter = SectionsPagerAdapter(fm)
        pagerAdapter.addFragment(CameraFragment())
        pagerAdapter.addFragment(HomeFragment())
        //pagerAdapter.addActivity(HomeActivityViewPager())
        pager_main.offscreenPageLimit = 2
        pager_main.adapter = pagerAdapter
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 3000)
    }
}