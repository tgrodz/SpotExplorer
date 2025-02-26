package com.spotexplorer.data.pref.source

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.spotexplorer.infrastructure.constants.AppConstant.DEFAULT_MARKER_SIZE
import com.spotexplorer.infrastructure.constants.AppConstant.PREF_NAME

object PreferencesDataSource {

    private const val KEY_MARKER_SIZE = "marker_size"
    private const val KEY_HAPTIC_EFFECT = "haptic_effect"
    private const val KEY_ROUTE_LINE = "route_line"
    private const val KEY_IMAGE_DARKER = "image_darker"
    private const val KEY_CHECKBOXES_STATE = "checkboxes_state"
    private const val KEY_ANIMATION_OPTION = "animation_option"
    private const val KEY_SHOW_TEXT = "show_text"
    private const val KEY_ENABLE_ANIMATION = "enable_animation"
    private const val KEY_DB_INITIALIZED = "hasInitializedDatabase"

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        Log.d("AppPreferences", "Initialized SharedPreferences: $PREF_NAME")
    }

    fun getMarkerSize(): Float = preferences.getFloat(KEY_MARKER_SIZE, DEFAULT_MARKER_SIZE)
    fun setMarkerSize(size: Float) {
        preferences.edit().putFloat(KEY_MARKER_SIZE, size).apply()
    }

    fun getHapticEffectStatus(): Boolean = preferences.getBoolean(KEY_HAPTIC_EFFECT, true)
    fun setHapticEffectStatus(isDisplay: Boolean) {
        preferences.edit().putBoolean(KEY_HAPTIC_EFFECT, isDisplay).apply()
    }

    fun getRouteLineStatus(): Boolean = preferences.getBoolean(KEY_ROUTE_LINE, true)
    fun setRouteLineStatus(isDisplay: Boolean) {
        preferences.edit().putBoolean(KEY_ROUTE_LINE, isDisplay).apply()
    }

    fun getImageDarkerStatus(): Boolean = preferences.getBoolean(KEY_IMAGE_DARKER, false)
    fun setImageDarkerStatus(isDisplay: Boolean) {
        preferences.edit().putBoolean(KEY_IMAGE_DARKER, isDisplay).apply()
    }

    fun getCheckboxesState(): List<Boolean> {
        val defaultState = "false,false"
        val stateString = preferences.getString(KEY_CHECKBOXES_STATE, defaultState)
        return stateString?.split(",")?.map { it.toBoolean() } ?: listOf(false, false)
    }
    fun setCheckboxesState(state: List<Boolean>) {
        val stateString = state.joinToString(separator = ",")
        preferences.edit().putString(KEY_CHECKBOXES_STATE, stateString).apply()
    }

    fun getAnimationOption(): Int = preferences.getInt(KEY_ANIMATION_OPTION, 3)
    fun setAnimationOption(option: Int) {
        preferences.edit().putInt(KEY_ANIMATION_OPTION, option).apply()
    }

    fun getShowText(): Boolean = preferences.getBoolean(KEY_SHOW_TEXT, true)
    fun setShowText(status: Boolean) {
        preferences.edit().putBoolean(KEY_SHOW_TEXT, status).apply()
    }

    fun getEnableAnimation(): Boolean = preferences.getBoolean(KEY_ENABLE_ANIMATION, true)
    fun setEnableAnimation(status: Boolean) {
        preferences.edit().putBoolean(KEY_ENABLE_ANIMATION, status).apply()
    }


    fun isDatabaseInitialized(): Boolean = preferences.getBoolean(KEY_DB_INITIALIZED, false)
    fun setDatabaseInitialized(initialized: Boolean) {
        preferences.edit().putBoolean(KEY_DB_INITIALIZED, initialized).apply()
    }
}
