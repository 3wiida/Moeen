package com.example.moeen.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moeen.utils.resultWrapper.ApiResult
import com.example.moeen.utils.resultWrapper.ResultWrapper
import com.google.android.gms.common.api.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private var _homeState: MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Loading)
    var homeState: StateFlow<ApiResult> =_homeState

    private var _postsState: MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Loading)
    var postsState: StateFlow<ApiResult> =_postsState

    private var _profileState: MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Loading)
    var profileState: StateFlow<ApiResult> = _profileState


    fun getHome() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val response = repository.getHome()){
                is ResultWrapper.Failure -> {
                    _homeState.value = ApiResult.Failure(response.code, response.message)
                }
                is ResultWrapper.Success -> {
                    _homeState.value = ApiResult.Success(response.results)
                }
            }
        }
    }

    fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val response = repository.getPosts()){
                is ResultWrapper.Failure -> {
                    _postsState.value = ApiResult.Failure(response.code, response.message)
                }
                is ResultWrapper.Success -> {
                    _postsState.value = ApiResult.Success(response.results)
                }
            }
        }
    }

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val response = repository.getProfile()){
                is ResultWrapper.Failure -> {
                    _profileState.value = ApiResult.Failure(response.code, response.message)
                }
                is ResultWrapper.Success -> {
                    _profileState.value = ApiResult.Success(response.results)
                }
            }
        }
    }
}