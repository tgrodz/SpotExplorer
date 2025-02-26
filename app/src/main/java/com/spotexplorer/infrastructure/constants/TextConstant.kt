package com.spotexplorer.infrastructure.constants

import androidx.compose.ui.graphics.Color


object AllocationScreenText {
    // Exit Dialog
    const val CONFIRMATION_REQUIRED = "Confirmation required"
    const val EXIT_DIALOG_TEXT = "Are you sure you want to go back? All unsaved data will be lost."
    const val YES = "Yes"
    const val NO = "No"

    // Loading
    const val LOADING_TEXT = "Uploading..."

    // TopAppBar
    const val ALLOCATION_TITLE = "Allocation"
    const val MENU = "Menu"
    const val OPEN_SETTINGS = "Open Settings"
    const val CLEAN_SELECTION = "Clean selection"

    // Screen labels
    const val ARRANGEMENT_SCREEN = "AllocationScreen"
    const val ALL_MARKERS_LABEL = "All markers: "
    const val MARKER_SIZE_LABEL = "Value from Pref. Marking Size: "

    // Image details
    const val IMAGE_INFORMATION = "Image information"
    const val IMAGE_NOT_LOADED = "Image not loaded or details not available."

    // Add Marker Dialog (Russian)
    const val ADD_MARKER_TITLE = "Adding a new marker"
    const val POSITION_LABEL = "Position: "
    const val MARKER_NAME_LABEL = "Marker name"
    const val ADD_BUTTON = "Add"
    const val CANCEL_RU = "Cancel"

    // List of Markers Card
    const val NAME_LABEL = "Name: "
    // Using a format string for coordinates
    const val COORDINATES_LABEL = "X: %s, Y: %s"
    const val STATUS_LABEL = "Status: "
    const val PIXEL_INDEX_LABEL = "Pixel Sequential Index: "

    // Toast message when clicking an empty space
    const val YOU_CLICKED_EMPTY = "No marker, empty place"

    // Marker Details Dialog (English)
    const val MARKER_DETAILS_TITLE = "Marker Details"
    const val NO_MARKER_FOUND = "No Marker Found"
    // For the Marker Details Dialog, we want a different Cancel text
    const val CANCEL_EN = "Cancel"
    const val VIEW_DETAILS = "View Details"
}



// Define constants for Marker Screen UI text
object MarkerScreenText {
    const val HEADER = "Marker Details"
    const val BACK_DESCRIPTION = "Back"
    const val SEQUENTIAL_NUMBER = "Sequential Number: "
    const val POSITION_Y = "Position Y: "
    const val POSITION_X = "Position X: "
    const val STATUS = "Status: "
    const val AVAILABLE_FEATURES = "Available Features"
    const val FREE = "Free"
    const val BUSY = "Busy"
    const val VIEW_REVIEWS = "View Reviews"
    const val CONFIRM = "Confirm"
    const val DELETE_MARKER = "Delete Marker"
    const val CONFIRM_DELETION_TITLE = "Confirm Deletion"
    const val CONFIRM_DELETION_TEXT =
        "Are you sure you want to delete this marker? This action cannot be undone."
    const val DELETE = "Delete"
    const val CANCEL = "Cancel"

    // Checkbox labels
    const val CHECKBOX_PUBLIC_TRANSPORT = "Public transport nearby"
    const val CHECKBOX_FURNITURE = "Furniture"
    const val CHECKBOX_BALCONY = "Balcony"
    val REVIEW_COLOR = Color(0xFFFFA500)
}

// Define constants for UI text
object NoteText {
    const val NOTE_HEADER = "Note"
    const val CLOSE_BUTTON = "Close"
    const val NO_DATA = "No data"
    const val NEW_NOTE_LABEL = "New Note"
    const val ADD_BUTTON = "Add"
    const val NOTE_PREFIX = "Note: "
}
object SettingsText {

    const val SETTINGS_TITLE = "Settings"
    const val BACK_BUTTON_DESCRIPTION = "Back"
    const val MARKER_SIZE_TITLE = "Marker size"
    const val SMALL_BUTTON = "Small"
    const val MEDIUM_BUTTON = "Medium"
    const val LARGE_BUTTON = "Large"
    const val SELECTED_MARKER_SIZE = "Selected size: %d px"

    const val HAPTIC_EFFECT_TITLE = "Haptic effect (vibration)"
    const val DRAW_ROUTE_TITLE = "Connecting line between markers"
    const val IMAGE_DARKER_TITLE = "Enable the image darker"

    const val ENABLE_ANIMATION_TITLE = "Enable animation"

    const val ANIMATION_OPTION = "Animation option: %d"

    const val SHOW_TEXT_TITLE = "Show text for markers"

    const val MARKER_SIZE_LOG = "Marker size: %d px"

}