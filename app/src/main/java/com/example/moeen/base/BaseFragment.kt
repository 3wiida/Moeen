package com.example.moeen.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.moeen.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

open class BaseFragment :Fragment() {
    lateinit var dialog:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        dialog= Dialog(requireContext())
        super.onCreate(savedInstanceState)
    }

    public fun showToast(c: Context, msg:String){
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show()
    }

    public fun loadingDialog(): Dialog {
        dialog.setContentView(R.layout.loading_dialog_layout)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.window?.attributes?.windowAnimations= R.style.loadingDialogAnimation
        return dialog
    }

    public fun start_activity(activity:Activity){
        startActivity(Intent(requireActivity(),activity::class.java))
    }
}