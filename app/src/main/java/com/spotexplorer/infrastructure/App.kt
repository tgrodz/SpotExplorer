package com.spotexplorer.infrastructure

import android.app.Application
import android.util.Log
import com.spotexplorer.data.database.AppDatabase
import com.spotexplorer.data.database.DatabaseProvider
import com.spotexplorer.data.pref.source.PreferencesDataSource
import com.spotexplorer.infrastructure.context.AppContextProvider
import com.spotexplorer.infrastructure.helper.DatabaseExtractor
import com.spotexplorer.infrastructure.helper.DatabaseInitializer
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        DatabaseProvider.init(this)
        PreferencesDataSource.init(this)
        AppContextProvider.init(this)

        prepareDatabase()
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        throw RuntimeException("Failed to prepare database! ${exception.localizedMessage}", exception)
    }

    private val singleThreadDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val sequentialScope = CoroutineScope(singleThreadDispatcher + coroutineExceptionHandler)

    private fun prepareDatabase() {
        sequentialScope.launch {
            val db = DatabaseProvider.getDatabase()
            if (!PreferencesDataSource.isDatabaseInitialized()) {
                initializeDatabase(db)
            } else {
                Log.d("App", "Database already initialized, skipping initialization.")
            }
            finalizeDatabaseExtraction(db)
        }
    }

    private suspend fun initializeDatabase(db: AppDatabase) {
        DatabaseInitializer.initializeMarkersByAllocation(db)
        DatabaseInitializer.initializeNotes(db)
        PreferencesDataSource.setDatabaseInitialized(true)
        Log.d("App", "Database initialized successfully.")
    }

    private suspend fun finalizeDatabaseExtraction(db: AppDatabase) {
        DatabaseExtractor.Builder(db)
            .logSchema()
            .logTables(listOf("allocations", "markers", "notes"))
            .build()
    }

}
