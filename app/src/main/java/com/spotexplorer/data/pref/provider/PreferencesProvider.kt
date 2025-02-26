package com.spotexplorer.data.pref.provider

import kotlinx.coroutines.flow.StateFlow

interface PreferencesProvider {
        fun init()
        val checkboxesState: StateFlow<List<Boolean>>
        val markerSize: StateFlow<Float>
        val isHapticEffect: StateFlow<Boolean>
        val isRouteLine: StateFlow<Boolean>
        val isImageDarker: StateFlow<Boolean>
        val animationOption: StateFlow<Int>
        val showText: StateFlow<Boolean>
        val enableAnimation: StateFlow<Boolean>

        suspend fun toggleCheckbox(index: Int)
        suspend fun updateMarkerSize(size: Float)
        suspend fun updateHapticStatus(status: Boolean)
        suspend fun updateRouteLineStatus(status: Boolean)
        suspend fun updateImageDarkerStatus(status: Boolean)
        suspend fun updateAnimationOption(option: Int)
        suspend fun updateShowText(status: Boolean)
        suspend fun updateEnableAnimation(status: Boolean)
}
