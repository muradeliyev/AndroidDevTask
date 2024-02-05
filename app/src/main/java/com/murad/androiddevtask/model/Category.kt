package com.murad.androiddevtask.model

import androidx.annotation.DrawableRes


/**
 * Created on 04 February 2024, 11:33 AM
 * @author Murad Eliyev
 */


data class Category(
    val id: Long,
    @DrawableRes val icon: Int,
    val name: String,
    val percentage: Int,
    val amount: Int
)
