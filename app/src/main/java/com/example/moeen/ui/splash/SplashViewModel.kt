package com.example.moeen.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.moeen.utils.PrefUtils.PrefKeys.IS_FIRST_TIME
import com.example.moeen.utils.PrefUtils.PrefKeys.USER_TOKEN
import com.example.moeen.utils.PrefUtils.PrefUtils.Companion.getFromPref
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(@ApplicationContext val context: Context) : ViewModel() {
    fun isLoggedIn() = getFromPref(context, USER_TOKEN, "") != ""
    fun isFirstTime() = getFromPref(context, IS_FIRST_TIME, true)
}


