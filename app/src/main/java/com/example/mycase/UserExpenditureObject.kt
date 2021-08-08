package com.example.mycase

import com.google.firebase.Timestamp
import java.util.*

class UserExpenditureObject {

    var email : String? = null
    var explanation : String? = null
    var expenditure : Int? = null
    var date : Date? = null

    constructor()

    constructor(email : String, explanation : String, expenditure : Int, date : Date){
        this.email = email
        this.explanation = explanation
        this.expenditure = expenditure
        this.date = date
    }
}