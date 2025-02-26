package com.spotexplorer.presentation.shared

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.spotexplorer.data.pref.provider.AndroidPreferencesProvider

class SettingsSharedViewModel() : BaseViewModel() {

    private val preferencesProvider = AndroidPreferencesProvider()

    init {
        preferencesProvider.init()
    }

    private val _markerSize = MutableStateFlow(preferencesProvider.markerSize.value)
    val markerSize: StateFlow<Float> get() = _markerSize

    private val _useHapticEffect = MutableStateFlow(preferencesProvider.isHapticEffect.value)
    val useHapticEffect: StateFlow<Boolean> get() = _useHapticEffect

    private val _useRouteLine = MutableStateFlow(preferencesProvider.isRouteLine.value)
    val useRouteLine: StateFlow<Boolean> get() = _useRouteLine

    private val _animationOption = MutableStateFlow(preferencesProvider.animationOption.value)
    val animationOption: StateFlow<Int> get() = _animationOption

    private val _isShowText = MutableStateFlow(preferencesProvider.showText.value)
    val isShowText: StateFlow<Boolean> get() = _isShowText

    private val _enableAnimation = MutableStateFlow(preferencesProvider.enableAnimation.value)
    val enableAnimation: StateFlow<Boolean> get() = _enableAnimation

    private val _enableImageDarker = MutableStateFlow(preferencesProvider.isImageDarker.value)
    val enableImageDarker: StateFlow<Boolean> get() = _enableImageDarker

    fun updateMarkerSize(size: Float) {
        launchInBackground {
            _markerSize.value = size
            preferencesProvider.updateMarkerSize(size)
        }
    }

    fun updateUseHapticEffect(status: Boolean) {
        launchInBackground {
            _useHapticEffect.value = status
            preferencesProvider.updateHapticStatus(status)
        }
    }

    fun updateUseRouteLine(status: Boolean) {
        launchInBackground {
            _useRouteLine.value = status
            preferencesProvider.updateRouteLineStatus(status)
        }
    }

    fun updateAnimationOption(animationOption: Int) {
        launchInBackground {
            _animationOption.value = animationOption
            preferencesProvider.updateAnimationOption(animationOption)
        }
    }

    fun updateShowText(status: Boolean) {
        launchInBackground {
            _isShowText.value = status
            preferencesProvider.updateShowText(status)
        }
    }

    fun updateEnableAnimation(enableAnimation: Boolean) {
        launchInBackground {
            _enableAnimation.value = enableAnimation
            preferencesProvider.updateEnableAnimation(enableAnimation)
        }
    }

    fun updateEnableImageDarker(status: Boolean) {
        launchInBackground {
            _enableImageDarker.value = status
            preferencesProvider.updateImageDarkerStatus(status)
        }
    }
}
