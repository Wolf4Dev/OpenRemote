package com.example.openremote.callapi.repository

import com.example.openremote.callapi.apis.AssetApiProvider
import com.example.openremote.callapi.apis.AssetInterface

class AssetRepository {
    private val retrofit = AssetApiProvider().getRetrofit()
    val service = retrofit.create(AssetInterface::class.java)
}