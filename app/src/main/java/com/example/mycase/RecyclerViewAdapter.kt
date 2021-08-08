package com.example.mycase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

class RecyclerViewAdapter() : RecyclerView.Adapter<RecyclerViewAdapter.ExpenditureHolder>() {
    lateinit var explanationList : ArrayList<String>
    lateinit var expendituresList: ArrayList<String>
    lateinit var listener: OnItemClickListener
    constructor(explanationList: ArrayList<String>,expendituresList: ArrayList<String>, listener: OnItemClickListener) : this() {
        this.explanationList = explanationList
        this.expendituresList = expendituresList
        this.listener = listener
    }

    inner class ExpenditureHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var recyclerExplanationText : TextView? = null
        var recyclerExpenditureText : TextView? = null
        var recyclerButtonDelete : Button? = null

        init {
            recyclerExplanationText = view.findViewById(R.id.textViewCardExplanation)
            recyclerExpenditureText = view.findViewById(R.id.textViewCardExpenditure)
            recyclerButtonDelete = view.findViewById(R.id.buttonDelete)
            view.setOnClickListener(this)
            recyclerButtonDelete!!.setOnClickListener(this)
        }

        override fun onClick(v: View?){
            val position : Int = adapterPosition

            if(position != RecyclerView.NO_POSITION){
                if (v?.id == recyclerButtonDelete?.id){
                    listener.onItemClick(position)

                }else
                    listener.onItemClick(position)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenditureHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cardview_expenditure_design, parent, false)
        return ExpenditureHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenditureHolder, position: Int) {
        holder.recyclerExplanationText?.text = explanationList[position]
        holder.recyclerExpenditureText?.text = expendituresList[position]
    }

    override fun getItemCount(): Int {
        return explanationList.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}