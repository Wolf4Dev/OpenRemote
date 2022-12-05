package com.example.openremote.callapi.model

data class Format(
    val asOnOff: Boolean,
    val asOpenClosed: Boolean,
    val asSlider: Boolean,
    val maximumFractionDigits: Int,
    val minimumFractionDigits: Int,
    val multiline: Boolean
)