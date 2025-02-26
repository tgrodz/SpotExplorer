package com.spotexplorer

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.spotexplorer.data.database.AppDatabase
import com.spotexplorer.data.database.dao.AllocationDao
import com.spotexplorer.data.database.dao.MarkerDao
import com.spotexplorer.model.entity.AllocationEntity
import com.spotexplorer.model.entity.marker.MarkerEntity
import com.spotexplorer.model.entity.marker.MarkerStatus
import com.spotexplorer.model.entity.marker.MarkerType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MarkerDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var markerDao: MarkerDao
    private lateinit var allocationDao: AllocationDao
    private var allocationId: Int = -1

    @Before
    fun setup() = runBlocking {

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        markerDao = database.markerDao()
        allocationDao = database.allocationDao()


        val allocation = AllocationEntity(
            sectionID = 1,
            placementUrl = "dummy_url",
            isSynced = false
        )
        allocationId = allocationDao.insertAllocation(allocation).toInt()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndGetMarker() = runBlocking {
        val marker = MarkerEntity(
            allocationId = allocationId,
            name = "Test Marker",
            xPoint = 100f,
            yPoint = 200f,
            pixelSequentialNumberPoint = 1,
            status = MarkerStatus.YELLOW,
            type = MarkerType.UNKNOWN,
            hasPublicTransport = false,
            hasFurniture = false,
            hasBalcony = false
        )
        val id = markerDao.insertMarker(marker)
        val fetched = markerDao.getMarkerById(id.toInt())
        assertNotNull(fetched)
        assertEquals("Test Marker", fetched?.name)
        assertEquals(100f, fetched?.xPoint)
    }

    @Test
    fun testUpdateMarker() = runBlocking {
        val marker = MarkerEntity(
            allocationId = allocationId,
            name = "Test Marker",
            xPoint = 100f,
            yPoint = 200f,
            pixelSequentialNumberPoint = 1,
            status = MarkerStatus.YELLOW,
            type = MarkerType.UNKNOWN,
            hasPublicTransport = false,
            hasFurniture = false,
            hasBalcony = false
        )
        val id = markerDao.insertMarker(marker)

        val updatedMarker = marker.copy(id = id.toInt(), name = "Updated Marker")
        val rows = markerDao.updateMarker(updatedMarker)
        assertEquals(1, rows)
        val fetched = markerDao.getMarkerById(id.toInt())
        assertEquals("Updated Marker", fetched?.name)
    }

    @Test
    fun testDeleteMarker() = runBlocking {
        val marker = MarkerEntity(
            allocationId = allocationId,
            name = "Test Marker",
            xPoint = 100f,
            yPoint = 200f,
            pixelSequentialNumberPoint = 1,
            status = MarkerStatus.YELLOW,
            type = MarkerType.UNKNOWN,
            hasPublicTransport = false,
            hasFurniture = false,
            hasBalcony = false
        )
        val id = markerDao.insertMarker(marker)
        markerDao.deleteMarkerById(id.toInt())
        val fetched = markerDao.getMarkerById(id.toInt())
        assertNull(fetched)
    }
}
