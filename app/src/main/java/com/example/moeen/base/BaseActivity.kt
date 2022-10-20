package com.example.moeen.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moeen.R

open class BaseActivity : AppCompatActivity() {

    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        dialog= Dialog(this)
        super.onCreate(savedInstanceState)
    }

    fun showToast(c: Context, msg:String){
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show()
    }

    fun loadingDialog(): Dialog {
        dialog.setContentView(R.layout.loading_dialog_layout)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.window?.attributes?.windowAnimations= R.style.loadingDialogAnimation
        return dialog
    }

}