package com.android.socialmedia.NavigationBottom.Activity.Fragment

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.socialmedia.MainActivity
import com.android.socialmedia.Model.album.Albums
import com.android.socialmedia.R
import com.android.socialmedia.RecyclerViewAdapter.album.SingleAlbumAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_activity_user_gallery_layout.*

class UserGallery : Fragment() {

    var activityUserGallery : Activity = Activity()
    var fragmentUserGallery : FragmentActivity = FragmentActivity()
    var adapter: SingleAlbumAdapter? = null

    //Creating MediaController
    var mediaController : MediaController? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activityUserGallery = activity
        fragmentUserGallery = activity as FragmentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.activity_activity_user_gallery_layout, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = DisplayMetrics()
        activityUserGallery.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        mediaController = MediaController(activityUserGallery)
        init_ui_views(height, width)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {

    }

    fun setupBackToBottomNavigationView(cls : Class<*>){
        //for two user or provider site
        val i = Intent(activityUserGallery, cls)
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        activityUserGallery.finish()
    }

    private fun init_ui_views(height : Int, width : Int) {

        close_gallery.setOnClickListener {
            setupBackToBottomNavigationView(MainActivity::class.java)
        }

        next_gallery.setOnClickListener {
            //use next page
            Log.i("eventClick", "yeah click")
        }

        val options = RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).override(width/ 4, width / 4).skipMemoryCache(true).error(R.drawable.ic_launcher_background)
        val glide = Glide.with(activityUserGallery)
        val builder = glide.asBitmap()

        rvAlbumSelected.layoutManager = GridLayoutManager(activityUserGallery, 4)
        rvAlbumSelected?.setHasFixedSize(true)
        val allGallery: MutableList<String> = getAllShownImagesPath(activityUserGallery)

        //show default show image
        gallery_show_image.cropInfo
        gallery_show_image.loadedBitmap
        gallery_show_image.setShowAnimation(true)

        if (allGallery.get(0).contains(".mp4")){
            gallery_show_video.visibility = View.VISIBLE
            gallery_show_image.visibility = View.GONE

            var myUri : Uri = Uri.parse(allGallery.get(0))
            mediaController?.setAnchorView(gallery_show_video)
            //Setting MediaController and URI, then starting the videoView
            gallery_show_video.setMediaController(mediaController)
            gallery_show_video.setVideoURI(myUri)
            gallery_show_video.requestFocus()
            //gallery_show_video.start()


        } else {
            gallery_show_image.visibility = View.VISIBLE
            gallery_show_video.visibility = View.GONE

            glide.load(allGallery.get(0))
                .centerCrop()
                .into(gallery_show_image)
        }

        adapter = object : SingleAlbumAdapter(allGallery, activityUserGallery, options, builder, glide) {
            override fun selectedImage(position: Int) {
                Log.e("ClickSingleAlbum", "yeah click image = " + allGallery.get(position))
                if (allGallery.get(position).contains(".mp4")){
                    gallery_show_video.visibility = View.VISIBLE
                    gallery_show_image.visibility = View.GONE

                    var myUri : Uri = Uri.parse(allGallery.get(position))
                    mediaController?.setAnchorView(gallery_show_video)
                    //Setting MediaController and URI, then starting the videoView
                    gallery_show_video.setMediaController(mediaController)
                    gallery_show_video.setVideoURI(myUri)
                    gallery_show_video.requestFocus()
                    //gallery_show_video.start()
                } else {
                    gallery_show_image.visibility = View.VISIBLE
                    gallery_show_video.visibility = View.GONE

                    glide.load(allGallery.get(position))
                        .centerCrop()
                        .into(gallery_show_image)
                }
            }
        }
        rvAlbumSelected?.adapter = adapter

        rvAlbumSelected?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        app_bar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                } else if (isShow) {
                    isShow = false
                }
            }
        })
    }

    // Read all images path from specified directory.
    private fun getAllShownImagesPath(activity: Activity): MutableList<String> {

        val BUCKET_GROUP_BY = "1) GROUP BY 1,(2"
        val BUCKET_ORDER_BY = "MAX(datetaken) DESC"

        //get image
        val uri_image: Uri
        val cursorBucket_image: Cursor
        val column_index_data: Int
        val listOfAllGallery = ArrayList<String>()
        var absolutePathOfImage: String? = null

        uri_image = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projectionOnlyBucket = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        cursorBucket_image =
            activity.contentResolver.query(uri_image, projectionOnlyBucket, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY)!!

        column_index_data = cursorBucket_image.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        while (cursorBucket_image.moveToNext()) {
            absolutePathOfImage = cursorBucket_image.getString(column_index_data)
            if (absolutePathOfImage != "" && absolutePathOfImage != null) {
                listOfAllGallery.add(absolutePathOfImage)
            }
        }

        //get video
        var cursor_video: Cursor
        var cursorBucket_video: Cursor
        var uri_video: Uri
        val column_index_album_name: Int
        val column_index_album_video: Int
        val albumsListVideo = ArrayList<Albums>()

        uri_video = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val projection_video = arrayOf(MediaStore.Video.VideoColumns.BUCKET_ID,
            MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Video.VideoColumns.DATE_TAKEN,
            MediaStore.Video.VideoColumns.DATA)

        cursor_video = activityUserGallery.contentResolver.query(uri_video, projection_video, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY)!!

        if (cursor_video != null) {
            column_index_album_name = cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            column_index_album_video = cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            while (cursor_video.moveToNext()) {
                Log.d("title_apps", "bucket video:" + cursor_video.getString(column_index_album_name))
                Log.d("title_apps", "bucket video:" + cursor_video.getString(column_index_album_video))
                val selectionArgs = arrayOf("%" + cursor_video.getString(column_index_album_name) + "%")

                val selection = MediaStore.Video.Media.DATA + " like ? "
                val projectionOnlyBucket = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

                cursorBucket_video = activityUserGallery.contentResolver.query(uri_video, projectionOnlyBucket, selection, selectionArgs, null)!!
                Log.d("title_apps", "bucket size:" + cursorBucket_video.count)

                albumsListVideo.add(Albums(cursor_video.getString(column_index_album_name), cursor_video.getString(column_index_album_video), cursorBucket_video.count, true))
            }

            for (album in albumsListVideo) {
                album.imagePath?.let { listOfAllGallery.add(it) }
            }
        }
        return listOfAllGallery.asReversed()
    }


}