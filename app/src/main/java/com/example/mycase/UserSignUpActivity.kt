package com.example.mycase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_sign_up.*

class UserSignUpActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sign_up)

        auth = FirebaseAuth.getInstance()
        buttonUserSingUp.setOnClickListener { userSignUpClicked() }

    }

    fun userSignUpClicked(){
        val email = editTextRegisterEmailAddress.text.toString()
        val password = editTextRegisterPassword.text.toString()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(applicationContext,"Sign up successful!",Toast.LENGTH_LONG).show()

                finish()
            }
        }.addOnFailureListener { exception ->
            if (exception != null){
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}