package com.example.mycase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.cardview_expenditure_design.*

class HomeActivity : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    private var userObject = UserExpenditureObject()
    private var userObjectList : ArrayList<UserExpenditureObject> = ArrayList()
    private var expendituresList : ArrayList<String> = ArrayList()
    private var explanationsList : ArrayList<String> = ArrayList()
    private var sumExpendituresList : ArrayList<Int> = ArrayList()

    private lateinit var adapter : RecyclerViewAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var currentEmail : String
    private var currentRevenue : String ="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        try {
            auth = FirebaseAuth.getInstance()
            db = FirebaseFirestore.getInstance()

            getDataFromFirestore()
            nameTrim()

            layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            adapter = RecyclerViewAdapter(explanationsList, expendituresList, this)
            recyclerView.adapter = adapter
        }catch (e: Exception){
            println(e)
        }

        fab.setOnClickListener {
            val intent = Intent(applicationContext, AddExpenditureActivity::class.java)
            startActivity(intent)
        }
    }

    fun getDataFromFirestore(){

        db.collection("Users")
            .whereEqualTo("email", auth.currentUser!!.email)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val email = document.get("email") as String
                    val explanation = document.get("explanation") as String
                    val expenditures = document.get("expenditure") as String
                    val timestamp = document.get("date") as Timestamp
                    userObject.email = email
                    userObject.explanation = explanation
                    userObject.expenditure = expenditures.toInt()
                    sumExpendituresList.add(expenditures.toInt())
                    userObject.date  = timestamp.toDate()
                    userObjectList.add(userObject)

                    explanationsList.add(explanation)
                    expendituresList.add(expenditures)

                    println(userObject.email)
                    println(userObject.explanation)
                    println(userObject.expenditure)
                    println(userObject.date)

                    adapter!!.notifyDataSetChanged()
                }
                getRevenueFromFirestore()

            }
            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }
    }

    fun nameTrim(){
        val tempName = auth.currentUser!!.email
        val name = tempName!!.substringBeforeLast('@', "Username not found")
        textViewIsim.text = name
    }

    fun getRevenueFromFirestore(){
        db.collection("UsersRevenue")
            .whereEqualTo("email", auth.currentUser!!.email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    currentEmail = document.get("email") as String
                    currentRevenue = document.get("revenue") as String
                }
                textViewTotalRevenue.text = currentRevenue
                sumExpenditures(sumExpendituresList)
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }
    }

    fun sumExpenditures(list : ArrayList<Int>){
        var totalExpenditure : Int = 0
        if(!list.isEmpty()){
            var i = 0
            while(i < list.size){
                totalExpenditure += list[i]
                println(list[i])
                i++
            }
        }else{
            println("List is Empty")
        }
        textViewTotalExpenditure.text = totalExpenditure.toString()

        var remaining = currentRevenue.toInt() - totalExpenditure
        if(remaining == 0){
            textViewRemainingEdit.text = totalExpenditure.toString()
        }else if(remaining > 0){
            textViewRemainingEdit.text = "+" + Math.abs(remaining).toString()
        }else{
            textViewRemainingEdit.text = "-" + Math.abs(remaining).toString()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInlfater = menuInflater
        menuInlfater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            auth.signOut()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onItemClick(position: Int) {
        //recycler view on item click
    }

}