package com.example.openremote.callapi.model

data class AttributeDescriptor(
    val constraints: List<Constraint>,
    val format: Format,
    val meta: Meta,
    val name: String,
    val optional: Boolean,
    val type: String,
    val units: List<String>
)