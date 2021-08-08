package com.example.mycase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_expenditure.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.math.round

class AddExpenditureActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expenditure)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        buttonAdd.setOnClickListener { addClicked() }
        buttonRevenueAdd.setOnClickListener { revenueAddClicked() }
    }

    fun revenueAddClicked(){
        val email = auth.currentUser!!.email
        val revenue = editTextRevenue.text.toString()

        val userRevenueMap = hashMapOf<String, Any>()
        if(revenue != "" ) {
            if (email != null) {
                userRevenueMap.put("email", email)
            }
            userRevenueMap.put("revenue", revenue)

            db.collection("UsersRevenue").add(userRevenueMap).addOnCompleteListener { task ->
                if(task.isComplete && task.isSuccessful){
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage.toString(),Toast.LENGTH_LONG).show()
            }
        }
        else{
            Toast.makeText(applicationContext,"Boxes cannot remain empty. Please fill in all boxes.", Toast.LENGTH_LONG).show()
        }
    }


    fun addClicked(){

        val userMap = hashMapOf<String, Any>()
        val email = auth.currentUser!!.email
        val explanation = editTextExplanation.text.toString()
        val expenditure = editTextExpenditure.text.toString()

        if (email != null && explanation != "" &&  expenditure != "") {
            userMap.put("email", email)
            userMap.put("explanation", explanation)
            userMap.put("expenditure", expenditure)
            userMap.put("date", Timestamp.now())

            db.collection("Users").add(userMap).addOnCompleteListener { task ->
              if(task.isComplete && task.isSuccessful){
                  val intent = Intent(applicationContext, HomeActivity::class.java)
                  startActivity(intent)
                  finish()
              }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage.toString(),Toast.LENGTH_LONG).show()
            }
        }
        else{
            Toast.makeText(applicationContext,"Boxes cannot remain empty. Please fill in all boxes.", Toast.LENGTH_LONG).show()
        }


    }
}