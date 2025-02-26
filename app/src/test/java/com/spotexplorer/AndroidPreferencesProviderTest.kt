package com.spotexplorer

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.spotexplorer.data.pref.provider.AndroidPreferencesProvider
import com.spotexplorer.data.pref.source.PreferencesDataSource
import com.spotexplorer.infrastructure.context.AppContextProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.junit.runner.RunWith

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AndroidPreferencesProviderTest {

    private lateinit var provider: AndroidPreferencesProvider


    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        AppContextProvider.init(context)
        PreferencesDataSource.init(context)
        provider = AndroidPreferencesProvider()
        provider.init()
    }


    @Test
    fun testUpdateMarkerSize() = runTest {
        val newSize = 25f
        provider.updateMarkerSize(newSize)
        val markerSize = provider.markerSize.first()
        assertEquals(newSize, markerSize, 0.0f)
    }

    @Test
    fun testToggleCheckbox() = runTest {
        val initialState = provider.checkboxesState.first()
        provider.toggleCheckbox(1)
        val newState = provider.checkboxesState.first()
        val expected = initialState.toMutableList().apply { this[1] = !this[1] }
        assertEquals(expected, newState)
    }

    @Test
    fun testUpdateHapticStatus() = runTest {
        val newStatus = true
        provider.updateHapticStatus(newStatus)
        val isHaptic = provider.isHapticEffect.first()
        assertEquals(newStatus, isHaptic)
    }

    @Test
    fun testUpdateRouteLineStatus() = runTest {
        val newStatus = true
        provider.updateRouteLineStatus(newStatus)
        val isRouteLine = provider.isRouteLine.first()
        assertEquals(newStatus, isRouteLine)
    }

    @Test
    fun testUpdateImageDarkerStatus() = runTest {
        val newStatus = true
        provider.updateImageDarkerStatus(newStatus)
        val isImageDarker = provider.isImageDarker.first()
        assertEquals(newStatus, isImageDarker)
    }

    @Test
    fun testUpdateAnimationOption() = runTest {
        val newOption = 2
        provider.updateAnimationOption(newOption)
        val option = provider.animationOption.first()
        assertEquals(newOption, option)
    }

    @Test
    fun testUpdateShowText() = runTest {
        val newStatus = true
        provider.updateShowText(newStatus)
        val showText = provider.showText.first()
        assertEquals(newStatus, showText)
    }

    @Test
    fun testUpdateEnableAnimation() = runTest {
        val newStatus = true
        provider.updateEnableAnimation(newStatus)
        val enableAnimation = provider.enableAnimation.first()
        assertEquals(newStatus, enableAnimation)
    }
}
