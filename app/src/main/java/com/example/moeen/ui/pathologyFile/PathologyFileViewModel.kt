package com.example.moeen.ui.pathologyFile

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.moeen.utils.PrefUtils.PrefKeys.USER_TOKEN
import com.example.moeen.utils.PrefUtils.PrefUtils.Companion.getFromPref
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PathologyFileViewModel @Inject constructor(val repo:PathologyRepository) :ViewModel() {
    fun isLoggedIn(context: Context):Boolean = getFromPref(context,USER_TOKEN,"") != ""
}