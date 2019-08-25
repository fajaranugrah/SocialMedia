package com.android.socialmedia.NavigationBottom.Activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.android.socialmedia.NavigationBottom.Activity.Fragment.UserGallery
import com.android.socialmedia.NavigationBottom.Activity.Fragment.UserPhoto
import com.android.socialmedia.PagerTransformer.SectionsPagerAdapter
import com.android.socialmedia.R
import com.google.android.material.tabs.TabLayout

class ActivityUser : AppCompatActivity() {

    val ACTIVITY_FRAGMENT : Int = 1
    var tabLayout_user : TabLayout? = null
    var viewPager_user : ViewPager? = null

    var pagerAdapter : SectionsPagerAdapter? = null

    val REQUEST_IMAGE_CAPTURE = 1
    private val PERMISSION_REQUEST_CODE: Int = 101

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    pagerAdapter!!.addFragment(UserPhoto())
                    pagerAdapter!!.addFragment(UserGallery())
                } else {
                    Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        tabLayout_user = findViewById<TabLayout>(R.id.tabLayout_user)
        viewPager_user = findViewById<ViewPager>(R.id.viewPager_user)

        tabLayout_user!!.addTab(tabLayout_user!!.newTab().setText("Gallery"))
        tabLayout_user!!.addTab(tabLayout_user!!.newTab().setText("Photo"))
        tabLayout_user!!.tabGravity = TabLayout.GRAVITY_FILL

        pagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        if (checkPersmission()){
            pagerAdapter!!.addFragment(UserGallery())
            pagerAdapter!!.addFragment(UserPhoto())
        } else {
            requestPermission()
        }
        viewPager_user!!.adapter = pagerAdapter

        viewPager_user!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout_user))
        tabLayout_user!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager_user!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE)
    }
}