package com.spotexplorer.infrastructure.context

import android.content.Context

object AppContextProvider {
    private lateinit var appContextProvider: ContextProvider

    fun init(context: Context) {
        appContextProvider = AndroidContextManager(context.applicationContext)
    }

    fun get(): ContextProvider {
        if (!AppContextProvider::appContextProvider.isInitialized) {
            throw IllegalStateException("ContextProvider not initialized. Call init() first.")
        }
        return appContextProvider
    }
}
