package com.example.openremote.callapi.apis

import com.example.openremote.callapi.model.Asset
import retrofit2.Response
import retrofit2.http.GET

interface AssetInterface {
    // https://103.126.161.199/api/master/model/assetInfos
    @GET("/master/model/assetInfos")
    suspend fun getAsset() : Response<Asset>
}