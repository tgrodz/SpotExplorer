package com.spotexplorer.data.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.spotexplorer.infrastructure.constants.AppConstant.DB_NAME
import com.spotexplorer.infrastructure.helper.DatabaseValidator
import kotlinx.coroutines.CompletableDeferred

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null
    private var isInitialized = false
    private var initDeferred = CompletableDeferred<Unit>()


    fun init(context: Context) {
        if (!isInitialized || INSTANCE == null) {
            synchronized(this) {
                if (!isInitialized || INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Log.d("DatabaseProvider", "Database created. onCreate callback triggered.")
                                DatabaseValidator.logTables(db)
                                DatabaseValidator.validateDatabaseSchema(db)
                            }

                            override fun onOpen(db: SupportSQLiteDatabase) {
                                super.onOpen(db)
                                Log.d("DatabaseProvider", "Database opened. onOpen callback triggered.")
                                if (!initDeferred.isCompleted) {
                                    initDeferred.complete(Unit)
                                }
                            }
                        })
                        .build()
                    isInitialized = true
                }
            }
        }
    }

    suspend fun awaitInitialized() {
        initDeferred.await()
    }


    fun getDatabase(): AppDatabase {
        return INSTANCE ?: throw IllegalStateException(
            "DatabaseProvider is not initialized. Call init(context) in the Application class."
        )
    }
}
