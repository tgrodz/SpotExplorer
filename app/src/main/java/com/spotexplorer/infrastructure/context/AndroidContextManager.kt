package com.spotexplorer.infrastructure.context

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

class AndroidContextManager(
    private val appContext: Context
) : ContextProvider {
    override val context: Context
        get() = appContext
    override val applicationContext: Context
        get() = appContext.applicationContext

    private fun getAppContext(): Context {
        return applicationContext ?: throw IllegalStateException(
            "ContextProvider is not initialized. Call init() in the Application class."
        )
    }

    override fun getString(resId: Int): String {
        return getAppContext().getString(resId)
    }

    override fun getDrawable(resId: Int): Drawable? {
        return ContextCompat.getDrawable(getAppContext(), resId)
    }
}

