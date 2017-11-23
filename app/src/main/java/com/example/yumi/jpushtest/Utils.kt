package com.example.yumi.jpushtest

import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View

var snackBar : Snackbar? = null

fun log(view : View?, msg : String?) {
    if(view != null) {
        if (snackBar != null && snackBar!!.isShown)
            snackBar!!.dismiss()
        snackBar = Snackbar.make(view, msg.toString(), Snackbar.LENGTH_SHORT)
        snackBar!!.show()
    }
    Log.e("TEST", msg.toString())
}