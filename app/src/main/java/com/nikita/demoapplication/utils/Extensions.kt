package com.nikita.demoapplication.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar


object Extensions {


    fun View.displaySnackbar(msg:String){
        if(msg.isNotEmpty()) {
            val snackbar = Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }



    fun Activity.hideKeyboard(){
        try {
            // get focused view:
            val view: View? = this.currentFocus
            view?.let{view->
                if (view.isFocused) {
                    val imm =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        } catch (e: Exception) {
            Log.d("hide_keyboard", e.message ?: "")
        }
    }


    fun Context.showToast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }
}

