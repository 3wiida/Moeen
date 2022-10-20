package com.example.moeen.ui.pathologyFile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import com.example.moeen.R
import com.example.moeen.databinding.ActivityPathologyFileBinding
import com.example.moeen.ui.Login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PathologyFile : AppCompatActivity() {
    lateinit var binding:ActivityPathologyFileBinding
    private val viewModel:PathologyFileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(viewModel.isLoggedIn(this)){
            binding=ActivityPathologyFileBinding.inflate(layoutInflater)
            setContentView(binding.root)

        }else{
            setContentView(R.layout.activity_pathology_file_nologin)
            val loginBtn=findViewById<Button>(R.id.pathologyFileLoginBtn)
            loginBtn.setOnClickListener{
                startActivity(Intent(this,LoginActivity::class.java))
            }
        }
    }
}