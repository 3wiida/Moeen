package com.example.moeen.ui.pathologyFile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moeen.R
import com.example.moeen.base.BaseActivity
import com.example.moeen.common.Constants.TAG
import com.example.moeen.databinding.ActivityPathologyFileBinding
import com.example.moeen.network.model.profileResponse.BloodType
import com.example.moeen.network.model.profileResponse.Country
import com.example.moeen.network.model.profileResponse.ProfileResponse
import com.example.moeen.network.model.profileResponse.Region
import com.example.moeen.ui.Login.LoginActivity
import com.example.moeen.ui.pathologyFile.adapters.BloodTypeSpinnerAdapter
import com.example.moeen.ui.pathologyFile.adapters.DiseasesSpinnerAdapter
import com.example.moeen.ui.pathologyFile.adapters.NationalitySpinnerAdapter
import com.example.moeen.ui.pathologyFile.adapters.RegionSpinnerAdapter
import com.example.moeen.ui.pathologyFile.pojo.NewProfileModel
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
                        is ApiResult.Success<*> -> {
                            val user = (it.data as ProfileResponse).data.user
                            binding.user = user
                            binding.bloodTypeSpinner.adapter=BloodTypeSpinnerAdapter(this@PathologyFileActivity,it.data.data.blood_types.toMutableList())
                            binding.chronicDiseases.adapter=DiseasesSpinnerAdapter(this@PathologyFileActivity,it.data.data.diseases.toMutableList())
                            binding.nationalitySpinner.adapter=NationalitySpinnerAdapter(this@PathologyFileActivity,it.data.data.countries.toMutableList())
                            binding.personRegion.adapter=RegionSpinnerAdapter(this@PathologyFileActivity,it.data.data.regions.toMutableList())

                            //binding.personRegion.setSelection(it.data.data.regions.indexOf(it.data.data.user.region))
                            loadingDialog().dismiss()
                        }
                    }
                }
            }

            /** save profile call */
            binding.pathologyFileSaveBTn.setOnClickListener {

                val name = binding.editTextTextPersonName.text.toString().trim()
                val nationalityId = binding.personId.text.toString()
                val weight = binding.personWeight.text.toString().toInt()
                val length = binding.personLength.text.toString().toInt()
                val insuranceNumber = binding.personTameenNumber.text.toString().trim()
                val bloodType=(binding.bloodTypeSpinner.selectedItem as BloodType).name!!
                val gender=if(binding.genderSpinner.selectedItem.toString() =="ذكر") "male" else "female"
                val nationality=(binding.nationalitySpinner.selectedItem as Country).nationality
                val notes=binding.personAdditionalInfo.text.toString()
                val region=(binding.personRegion.selectedItem as Region).id

                val newProfile = NewProfileModel(region, bloodType, name,gender , photo = null, nationalityId, nationality, weight, length, insuranceNumber,notes)
                viewModel.updateProfile(newProfile)

                lifecycleScope.launch {
                    viewModel.updateProfile.collect { result ->
                        when (result) {
                            ApiResult.Empty -> {}
                            is ApiResult.Failure -> {
                                loadingDialog().cancel()
                                showToast(this@PathologyFileActivity, result.message!!)
                            }
                            ApiResult.Loading -> loadingDialog().show()
                            is ApiResult.Success<*> -> {
                                loadingDialog().cancel()
                                showToast(this@PathologyFileActivity, "success")
                            }
                        }
                    }
                }
            }
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