package com.example.openremote.callapi.model

data class AssetDescriptor(
    val agentLinkType: String,
    val assetDiscovery: Boolean,
    val assetImport: Boolean,
    val colour: String,
    val descriptorType: String,
    val icon: String,
    val name: String
)