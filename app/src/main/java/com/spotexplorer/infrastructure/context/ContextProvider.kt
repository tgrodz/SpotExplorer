package com.spotexplorer.infrastructure.context

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface ContextProvider {
    val context: Context
    val applicationContext: Context

    fun getString(@StringRes resId: Int): String
    fun getDrawable(@DrawableRes resId: Int): Drawable?
}