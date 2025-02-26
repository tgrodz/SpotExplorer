package com.spotexplorer.infrastructure.helper

import android.util.Log
import com.spotexplorer.data.database.AppDatabase

object DatabaseExtractor {

    /**
     * Logs the schema of the database by printing each table name and its SQL statement.
     */
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

    /**
     * Logs the data of all tables specified in the list.
     */
    fun logAllTables(database: AppDatabase) {
        listOf("allocations", "markers", "notes").forEach { tableName ->
            logTableData(database, tableName)
        }
    }

    /**
     * Logs all data from a specific table.
     */
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
