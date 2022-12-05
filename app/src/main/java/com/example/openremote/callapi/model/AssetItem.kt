package com.example.openremote.callapi.model

data class AssetItem(
    val assetDescriptor: AssetDescriptor,
    val attributeDescriptors: List<AttributeDescriptor>,
    val metaItemDescriptors: List<String>,
    val valueDescriptors: List<String>
)