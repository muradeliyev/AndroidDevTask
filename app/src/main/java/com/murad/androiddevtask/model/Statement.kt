package com.murad.androiddevtask.model

import androidx.annotation.DrawableRes

data class Statement(
    @DrawableRes val icon: Int,
    val name: String,
    val time: String,
    val amount: Int
)
