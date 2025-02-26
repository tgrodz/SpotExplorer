package com.spotexplorer.data.pref.provider

import android.util.Log
import com.spotexplorer.data.pref.source.PreferencesDataSource
import com.spotexplorer.infrastructure.context.AppContextProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AndroidPreferencesProvider() : PreferencesProvider {

    private val _markerSize = MutableStateFlow(PreferencesDataSource.getMarkerSize())
    override val markerSize: StateFlow<Float> get() = _markerSize

    private val _checkboxesState = MutableStateFlow(PreferencesDataSource.getCheckboxesState())
    override val checkboxesState: StateFlow<List<Boolean>> get() = _checkboxesState

    private val _isHapticEffect = MutableStateFlow(PreferencesDataSource.getHapticEffectStatus())
    override val isHapticEffect: StateFlow<Boolean> get() = _isHapticEffect

    private val _isRouteLine = MutableStateFlow(PreferencesDataSource.getRouteLineStatus())
    override val isRouteLine: StateFlow<Boolean> get() = _isRouteLine

    private val _isImageDarker = MutableStateFlow(PreferencesDataSource.getImageDarkerStatus())
    override val isImageDarker: StateFlow<Boolean> get() = _isImageDarker

    private val _animationOption = MutableStateFlow(PreferencesDataSource.getAnimationOption())
    override val animationOption: StateFlow<Int> get() = _animationOption

    private val _showText = MutableStateFlow(PreferencesDataSource.getShowText())
    override val showText: StateFlow<Boolean> get() = _showText

    private val _enableAnimation = MutableStateFlow(PreferencesDataSource.getEnableAnimation())
    override val enableAnimation: StateFlow<Boolean> get() = _enableAnimation

    override fun init() {
        PreferencesDataSource.init(AppContextProvider.get().applicationContext)
        logSettings()
    }

    override suspend fun toggleCheckbox(index: Int) {
        val newState = _checkboxesState.value.toMutableList().also {
            it[index] = !it[index]
        }
        _checkboxesState.value = newState
        PreferencesDataSource.setCheckboxesState(newState)
    }

    override suspend fun updateMarkerSize(size: Float) {
        PreferencesDataSource.setMarkerSize(size)
        _markerSize.value = size
    }

    override suspend fun updateHapticStatus(status: Boolean) {
        PreferencesDataSource.setHapticEffectStatus(status)
        _isHapticEffect.value = status
    }

    override suspend fun updateRouteLineStatus(status: Boolean) {
        PreferencesDataSource.setRouteLineStatus(status)
        _isRouteLine.value = status
    }

    override suspend fun updateImageDarkerStatus(status: Boolean) {
        PreferencesDataSource.setImageDarkerStatus(status)
        _isImageDarker.value = status
    }

    override suspend fun updateAnimationOption(option: Int) {
        PreferencesDataSource.setAnimationOption(option)
        _animationOption.value = option
    }

    override suspend fun updateShowText(status: Boolean) {
        PreferencesDataSource.setShowText(status)
        _showText.value = status
    }

    override suspend fun updateEnableAnimation(status: Boolean) {
        PreferencesDataSource.setEnableAnimation(status)
        _enableAnimation.value = status
    }

    fun logSettings() {
        Log.d("-- SettingsProvider", "Marker size: ${PreferencesDataSource.getMarkerSize()}")
        Log.d("-- SettingsProvider", "Haptic effect is activated: ${PreferencesDataSource.getHapticEffectStatus()}")
        Log.d("-- SettingsProvider", "Route line is activated: ${PreferencesDataSource.getRouteLineStatus()}")
        Log.d("-- SettingsProvider", "Animation option: ${PreferencesDataSource.getAnimationOption()}")
        Log.d("-- SettingsProvider", "Show text: ${PreferencesDataSource.getShowText()}")
        Log.d("-- SettingsProvider", "Enable animation: ${PreferencesDataSource.getEnableAnimation()}")
    }
}
