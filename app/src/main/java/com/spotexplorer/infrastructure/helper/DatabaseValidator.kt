package com.spotexplorer.infrastructure.helper

import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Simple DatabaseValidator in logs
 *
 * The `DatabaseValidator` class is a helper used to verify that your database schema matches
 * the expected structure defined in your app.
 * It directly accesses the underlying SQLite database (via `SupportSQLiteDatabase`),
 * logs all table names, and validates that each table contains the expected columns.
 * This is useful for quickly checking your schema during development.
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
     * Throws an exception if some columns are missing.
     */
    fun validateDatabaseSchema(db: SupportSQLiteDatabase) {
        // Validate "allocations" table
        validateTableSchema(db, "allocations", listOf("id", "section_id", "placement_url"))

        // Validate "markers" table
        validateTableSchema(
            db,
            "markers",
            listOf(
                "id", "allocation_id", "name", "x_point", "y_point",
                "sequential_number_point", "status", "type",
                "has_public_transport", "has_furniture", "has_balcony"
            )
        )

        // Validate "notes" table
        validateTableSchema(db, "notes", listOf("id", "marker_id", "title", "rating", "comment", "timestamp"))
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
