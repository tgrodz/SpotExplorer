package com.spotexplorer.infrastructure.helper

import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseValidator {

    /**
     * Logs all table names found in the database.
     */
    fun logTables(db: SupportSQLiteDatabase) {
        val cursor = db.query("SELECT name FROM sqlite_master WHERE type='table'")
        while (cursor.moveToNext()) {
            val tableName = cursor.getString(0)
            Log.d("DatabaseTables", "Found table: $tableName")
        }
        cursor.close()
    }

    /**
     * Validates the database schema for each table by comparing expected and actual columns.
     * Throws an exception if какие-либо столбцы отсутствуют.
     */
    fun validateDatabaseSchema(db: SupportSQLiteDatabase) {
        // Validate "allocations" table
        validateTableSchema(db, "allocations", listOf("id", "sectionID", "placementUrl", "isSynced"))
        // Validate "markers" table
        validateTableSchema(
            db,
            "markers",
            listOf(
                "id", "allocationId", "name", "pixel_x_point", "pixel_y_point",
                "pixel_sequential_number_point", "status", "type",
                "hasPublicTransport", "hasFurniture", "hasBalcony", "isSynced"
            )
        )
        // Validate "notes" table
        validateTableSchema(db, "notes", listOf("id", "markerId", "title", "rating", "comment", "timestamp"))
    }

    /**
     * Validates the schema of a single table using PRAGMA table_info.
     * Throws an exception if any expected column is missing.
     *
     * @param database The SQLite database instance.
     * @param tableName The name of the table to validate.
     * @param expectedColumns A list of expected column names.
     */
    private fun validateTableSchema(database: SupportSQLiteDatabase, tableName: String, expectedColumns: List<String>) {
        val cursor = database.query("PRAGMA table_info($tableName)")
        val existingColumns = mutableListOf<String>()
        while (cursor.moveToNext()) {
            val columnIndex = cursor.getColumnIndex("name")
            if (columnIndex != -1) {
                existingColumns.add(cursor.getString(columnIndex))
            }
        }
        cursor.close()

        val missingColumns = expectedColumns.filter { it !in existingColumns }
        if (missingColumns.isNotEmpty()) {
            throw IllegalStateException("The following columns are missing in table $tableName: $missingColumns")
        }
    }
}
