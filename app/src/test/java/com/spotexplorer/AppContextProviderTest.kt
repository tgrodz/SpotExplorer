package com.spotexplorer

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.spotexplorer.infrastructure.context.AppContextProvider
import com.spotexplorer.infrastructure.context.ContextProvider
import org.junit.runner.RunWith

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [28])
class AppContextProviderTest {

    private lateinit var appContext: Context

    @Before
    fun setup() {
        appContext = ApplicationProvider.getApplicationContext()
        AppContextProvider.init(appContext)
    }

    @Test
    fun testGetContextProvider() {
        val provider: ContextProvider = AppContextProvider.get()
        assertNotNull(provider)
        assertEquals(appContext.applicationContext, provider.applicationContext)
    }

    @Test
    fun testGetString() {
        val expected = appContext.getString(android.R.string.ok)
        val actual = AppContextProvider.get().getString(android.R.string.ok)
        assertEquals(expected, actual)
    }

    @Test
    fun testGetDrawable() {
        val expected = appContext.getDrawable(android.R.drawable.ic_delete)
        val actual = AppContextProvider.get().getDrawable(android.R.drawable.ic_delete)
        assertNotNull("Expected drawable should not be null", expected)
        assertNotNull("Actual drawable should not be null", actual)

        assertEquals(
            "Drawable classes should be equal",
            expected!!.javaClass,
            actual!!.javaClass
        )

    }

}
