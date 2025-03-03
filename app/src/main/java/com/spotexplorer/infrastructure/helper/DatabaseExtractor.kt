package com.spotexplorer.infrastructure.helper

import android.util.Log
import com.spotexplorer.data.database.AppDatabase

/**
 * Simple DatabaseExtractor in logs
 *
 * The `DatabaseExtractor` class is a helper used to verify that your database schema matches
 * the expected structure defined in your app.
 * It directly accesses the underlying SQLite database (via `SupportSQLiteDatabase`),
 *
 * However, for more robust and interactive development, it's recommended to use dedicated tools such as:
 *
 * Database Inspector:
 * Allows you to view your Room database in real time.
 * You can run queries, inspect tables,
 * and verify that your schema (including columns and data) is correct during debugging.
 *
 * Room Testing Library:
 * Provides testing artifacts (like `Room.inMemoryDatabaseBuilder`) to set up tests for
 * your database, including migration tests.
 * This helps you write unit tests to verify that your migrations and schema changes behave as expected.
 *
 * DB Browser for SQLite:
 * Lets you export your database file from your device or emulator and inspect it,
 * ensuring that the on-disk schema matches your expectations.
 *
 * This class serves primarily as a demonstration of
 * direct access to `SQLiteOpenHelper`-level functionality through `SupportSQLiteDatabase`.
 */
object DatabaseExtractor {


    fun logDatabaseSchema(database: AppDatabase) {
        try {
            val db = database.openHelper.writableDatabase
            val cursor = db.query("SELECT name, sql FROM sqlite_master WHERE type='table'")
            Log.d("DB_LOG", "================== DATABASE SCHEMA ==================")
            while (cursor.moveToNext()) {
                val tableName = cursor.getString(0)
                val tableSchema = cursor.getString(1)
                Log.d("DB_SCHEMA", "Table: $tableName\nSchema:\n$tableSchema")
            }
            cursor.close()
            Log.d("DB_LOG", "================== END OF SCHEMA ==================")
        } catch (e: Exception) {
            Log.e("DB_LOG_ERROR", "Error logging database schema: ${e.localizedMessage}", e)
        }
    }

    fun logAllTables(database: AppDatabase) {
        listOf("allocations", "markers", "notes").forEach { tableName ->
            logTableData(database, tableName)
        }
    }

    internal fun logTableData(database: AppDatabase, tableName: String) {
        try {
            val db = database.openHelper.writableDatabase

            val countCursor = db.query("SELECT COUNT(*) FROM $tableName")
            var rowCount = 0
            if (countCursor.moveToFirst()) {
                rowCount = countCursor.getInt(0)
            }
            countCursor.close()
            Log.d("DB_LOG", "=== TABLE: $tableName | Total Rows: $rowCount ===")

            val cursor = db.query("SELECT * FROM $tableName")
            if (cursor.count == 0) {
                Log.d("DB_DATA", "No data found in table: $tableName")
            } else {
                val columnCount = cursor.columnCount
                while (cursor.moveToNext()) {
                    val row = StringBuilder()
                    for (i in 0 until columnCount) {
                        row.append("${cursor.getColumnName(i)}: ${cursor.getString(i) ?: "NULL"} | ")
                    }
                    Log.d("DB_DATA", "Row: $row")
                }
            }
            cursor.close()
            Log.d("DB_LOG", "=== END OF TABLE: $tableName ===")
        } catch (e: Exception) {
            Log.e("DB_LOG_ERROR", "Error logging table $tableName: ${e.localizedMessage}", e)
        }
    }

    class Builder(private val database: AppDatabase) {
        private var shouldLogSchema: Boolean = false
        private var shouldLogAllTables: Boolean = false
        private var tablesToLog: List<String>? = null

        fun logSchema(): Builder {
            shouldLogSchema = true
            return this
        }

        fun logTables(tables: List<String>): Builder {
            tablesToLog = tables
            return this
        }

        fun logAllTables(): Builder {
            shouldLogAllTables = true
            return this
        }

        fun build() {
            if (shouldLogSchema) {
                logDatabaseSchema(database)
            }
            if (shouldLogAllTables) {
                logAllTables(database)
            } else if (tablesToLog != null) {
                tablesToLog!!.forEach { table ->
                    logTableData(database, table)
                }
            }
        }
    }
}
