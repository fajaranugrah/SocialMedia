package com.android.socialmedia.FirstScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.socialmedia.MainActivity
import com.android.socialmedia.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.layout_login_activity.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login_activity)

        auth = FirebaseAuth.getInstance()

        btn_login.setOnClickListener {
            if (login_username_or_email.text.equals("")){
                login_username_or_email.error = "Please Input Your Email or Username"
            } /*else if (login_username_or_email.text.matches()){

            }*/
            else if (login_password.text!!.equals("")){
                login_password.error = "Please Input Your Password"
            } else{
                auth.signInWithEmailAndPassword(login_username_or_email.text.toString(), login_password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("signInWithEmail:success", task.toString())
                            val user = auth.currentUser
                            //updateUI(user)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(0,0)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                            //updateUI(null)
                        }
                    }

            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //updateUI(currentUser)
    }


}