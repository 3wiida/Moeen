package com.example.moeen.ui.pathologyFile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moeen.R
import com.example.moeen.base.BaseActivity
import com.example.moeen.common.Constants.TAG
import com.example.moeen.databinding.ActivityPathologyFileBinding
import com.example.moeen.ui.Login.LoginActivity
import com.example.moeen.utils.PrefUtils.PrefKeys.USER_TOKEN
import com.example.moeen.utils.PrefUtils.PrefUtils.Companion.getFromPref
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PathologyFileActivity : BaseActivity() {
    lateinit var binding: ActivityPathologyFileBinding
    private val viewModel: PathologyFileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.isLoggedIn(this)) {
            binding = ActivityPathologyFileBinding.inflate(layoutInflater)
            setContentView(binding.root)

            //init dataBinding
            binding.viewModel = viewModel
            binding.lifecycleOwner = this

            //perform call to get profile data
            viewModel.getProfile()
            lifecycleScope.launchWhenCreated {
                viewModel.apisate.collect {
                    when (it) {
                        ApiResult.Empty -> {}
                        is ApiResult.Failure -> {
                            showToast(this@PathologyFileActivity, it.message!!)
                            loadingDialog().dismiss()
                        }
                        ApiResult.Loading -> loadingDialog().show()
                        is ApiResult.Success<*> -> loadingDialog().dismiss()
                    }
                }
            }

            //fire a call to get chronic diseases to put it in spinner
            viewModel.getChronicDiseases()

        }

        //this for if user skip login , will show this layout
        else {
            setContentView(R.layout.activity_pathology_file_nologin)
            val loginBtn = findViewById<Button>(R.id.pathologyFileLoginBtn)
            loginBtn.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
}