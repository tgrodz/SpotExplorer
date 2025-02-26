package com.spotexplorer

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.spotexplorer.data.database.AppDatabase
import com.spotexplorer.data.database.dao.AllocationDao
import com.spotexplorer.data.database.dao.MarkerDao
import com.spotexplorer.data.database.dao.NoteDao
import com.spotexplorer.model.entity.AllocationEntity
import com.spotexplorer.model.entity.NoteEntity
import com.spotexplorer.model.entity.marker.MarkerEntity
import com.spotexplorer.model.entity.marker.MarkerStatus
import com.spotexplorer.model.entity.marker.MarkerType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var noteDao: NoteDao
    private lateinit var markerDao: MarkerDao
    private lateinit var allocationDao: AllocationDao

    private var allocationId: Int = -1
    private var testMarkerId: Int = -1

    @Before
    fun setup() = runBlocking {

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        noteDao = database.noteDao()
        markerDao = database.markerDao()
        allocationDao = database.allocationDao()


        val allocation = AllocationEntity(
            sectionID = 1,
            placementUrl = "dummy_url",
            isSynced = false
        )
        allocationId = allocationDao.insertAllocation(allocation).toInt()


        val marker = MarkerEntity(
            allocationId = allocationId,
            name = "Test Marker",
            xPoint = 50f,
            yPoint = 50f,
            pixelSequentialNumberPoint = 1,
            status = MarkerStatus.YELLOW,
            type = MarkerType.UNKNOWN,
            hasPublicTransport = false,
            hasFurniture = false,
            hasBalcony = false
        )
        testMarkerId = markerDao.insertMarker(marker).toInt()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndGetNote() = runBlocking {

        val note = NoteEntity(
            markerId = testMarkerId,
            title = "Test User",
            rating = 4.5f,
            comment = "This is a test comment.",
            timestamp = Date()
        )

        val id = noteDao.insertNote(note)

        val notes = noteDao.getAllNotes().first()
        assertTrue("Expected non-empty notes list", notes.isNotEmpty())
        val fetchedNote = notes.first { it.id == id.toInt() }
        assertNotNull(fetchedNote)
        assertEquals("Test User", fetchedNote.title)
    }

    @Test
    fun testGetNotesForMarker() = runBlocking {

        val note1 = NoteEntity(
            markerId = testMarkerId,
            title = "User 1",
            rating = 3.0f,
            comment = "Comment 1",
            timestamp = Date()
        )
        val note2 = NoteEntity(
            markerId = testMarkerId,
            title = "User 2",
            rating = 4.0f,
            comment = "Comment 2",
            timestamp = Date(System.currentTimeMillis() + 1000)
        )

        noteDao.insertNote(note1)
        noteDao.insertNote(note2)


        val markerNotes = noteDao.getNotesForMarker(testMarkerId).first()

        assertEquals(2, markerNotes.size)
        assertEquals("User 2", markerNotes[0].title)
    }

    @Test
    fun testDeleteNote() = runBlocking {

        val note = NoteEntity(
            markerId = testMarkerId,
            title = "To be deleted",
            rating = 2.0f,
            comment = "Delete me",
            timestamp = Date()
        )
        val id = noteDao.insertNote(note)

        var notes = noteDao.getAllNotes().first()
        assertTrue(notes.any { it.id == id.toInt() })

        val fetchedNote = notes.first { it.id == id.toInt() }
        noteDao.deleteNote(fetchedNote)

        notes = noteDao.getAllNotes().first()
        assertTrue(notes.none { it.id == id.toInt() })
    }

    @Test
    fun testDeleteAllNotes() = runBlocking {

        val notesToInsert = listOf(
            NoteEntity(markerId = testMarkerId, title = "Note1", rating = 3f, comment = "A", timestamp = Date()),
            NoteEntity(markerId = testMarkerId, title = "Note2", rating = 4f, comment = "B", timestamp = Date())
        )
        noteDao.insertAll(notesToInsert)
        var notes = noteDao.getAllNotes().first()
        assertTrue(notes.isNotEmpty())

        noteDao.deleteAllNotes()
        notes = noteDao.getAllNotes().first()
        assertTrue(notes.isEmpty())
    }
}
