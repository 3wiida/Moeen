package com.example.moeen.ui.home.transportServices.ambulance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.moeen.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AmbulanceActivity : AppCompatActivity() {
    @Inject lateinit var bundle: Bundle
    private var whereAmI:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(whereAmI==0){
            finish()
            bundle.putSerializable("movingLocation",null)
            bundle.putSerializable("arrivalLocation",null)
        }else{
            val navController=findNavController(R.id.transportFragmentContainer)
            navController.navigateUp()
            setWhereAmI(0)
        }
    }

    fun setWhereAmI(i:Int){
        whereAmI=i
    }
}