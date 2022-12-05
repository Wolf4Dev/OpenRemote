package com.example.openremote.callapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openremote.callapi.model.Asset
import com.example.openremote.callapi.repository.AssetRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.Response

class AssetViewModel: ViewModel() {
    private val assetRepository = AssetRepository()
    private val _callApiResult = MutableLiveData<CallApiResult>()
    val callApiResult: MutableLiveData<CallApiResult> = _callApiResult

    sealed class CallApiResult {
        class ResultOk(val result: Response<Asset>): CallApiResult()
        object ResultError: CallApiResult()
    }

    fun callApi() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            val result = assetRepository.service.getAsset()
            if (result.isSuccessful) {
                callApiResult.value = CallApiResult.ResultOk(result)
            } else {
                callApiResult.value = CallApiResult.ResultError
            }
        }
    }

}