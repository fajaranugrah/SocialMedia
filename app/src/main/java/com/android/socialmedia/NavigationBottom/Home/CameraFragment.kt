package com.android.socialmedia.NavigationBottom.Home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.socialmedia.R
import com.android.socialmedia.UtilitiesManager.CameraPreview
import com.iammert.library.cameravideobuttonlib.CameraVideoButton
import kotlinx.android.synthetic.main.camera_fragment.*

class CameraFragment : Fragment(){

    var activityHome : Activity = Activity()

    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null

    val REQUEST_IMAGE_CAPTURE = 1
    private val PERMISSION_REQUEST_CODE: Int = 101

    // will talk about this flag later
    private var isVisibleToUser: Boolean = true

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(activityHome,"Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {

            }
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activityHome = activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.camera_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val videoRecordButton = findViewById<CameraVideoButton>(R.id.button)
        button_camera_story.setVideoDuration(10000)
        button_camera_story.actionListener = object : CameraVideoButton.ActionListener {
            override fun onCancelled() {
                Log.v("TEST", "onCancelled")
            }

            override fun onStartRecord() {
                Log.v("TEST", "onStartRecord")
            }

            override fun onEndRecord() {
                Log.v("TEST", "onEndRecord")
            }

            override fun onDurationTooShortError() {
                Log.v("TEST", "onDurationTooShortError")
            }

            override fun onSingleTap() {
                Log.v("TEST", "onSingleTap")
            }

        }

        Handler().postDelayed(Runnable {
            button_camera_story.cancelRecording()
        }, 5000)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        this.isVisibleToUser = isVisibleToUser

        if (isVisibleToUser){
            Log.v("TEST", "show camera")
            if (checkPersmission()){
                openCamera()
            } else {
                requestPermission()
            }
        } else{
            Log.v("TEST", "not show camera")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            //To get the File for further usage
            //val auxFile = File(mCurrentPhotoPath)

            //var bitmap: Bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
            //imageView.setImageBitmap(bitmap)

        }
    }

    fun openCamera() {
        // Create an instance of Camera
        mCamera = getCameraInstance()

        mPreview = mCamera?.let {
            // Create our Preview view
            CameraPreview(activityHome, it)
        }

        // Set the Preview view as the content of our activity.
        mPreview?.also {
            camera_preview.addView(it)
        }
    }

    /** A safe way to get an instance of the Camera object. */
    fun getCameraInstance(): Camera? {
        return try {
            releaseCameraAndPreview()
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }

    fun releaseCameraAndPreview() {
        //preview?.setCamera(null)
        mCamera?.also { camera ->
            camera.release()
            mCamera = null
        }
    }


    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(activityHome, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activityHome,
            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(activityHome, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE)
    }
}