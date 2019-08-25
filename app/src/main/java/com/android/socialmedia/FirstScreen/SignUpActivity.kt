package com.android.socialmedia.FirstScreen

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.socialmedia.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.layout_sign_up_activity.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_sign_up_activity)

        auth = FirebaseAuth.getInstance()

        btn_sign_up.setOnClickListener {
            if (sign_up_username.text.equals("")){
                sign_up_username.error = "Please Input Your Username"
            } else if (sign_up_email.text.equals("")){
                sign_up_email.error = "Please Input Your Email"
            } /*else if (login_username_or_email.text.matches()){

            }*/
            else if (sign_up_password.text!!.equals("")){
                sign_up_password.error = "Please Input Your Password"
            } else{
                auth.createUserWithEmailAndPassword(sign_up_email.text.toString(), sign_up_password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("createUserWithEmail:", "success " + task.toString())
                            val user = auth.currentUser
                            //updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("createUserWithEmail:", "failure " + task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
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