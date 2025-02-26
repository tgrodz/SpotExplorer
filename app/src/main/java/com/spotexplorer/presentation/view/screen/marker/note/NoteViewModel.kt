package com.spotexplorer.presentation.view.screen.marker.note

import androidx.lifecycle.viewModelScope
import com.spotexplorer.data.database.DatabaseProvider
import com.spotexplorer.model.entity.NoteEntity
import com.spotexplorer.presentation.shared.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.util.Date

class NoteViewModel : BaseViewModel() {

    private val noteDao = DatabaseProvider.getDatabase().noteDao()

    private val _markerIdFlow = MutableStateFlow<Int>(-1)

    val notesFlow = _markerIdFlow
        .flatMapLatest { markerId ->
            markerId.let { noteDao.getNotesForMarker(it) }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun setMarkerId(markerId: Int) {
        _markerIdFlow.value = markerId
    }

    fun addNote(markerId: Int, comment: String) {
        launchInBackground {
            val note = NoteEntity(
                markerId = markerId,
                title = "User",
                rating = 0f,
                comment = comment,
                timestamp = Date()
            )
            noteDao.insertNote(note)
        }
    }

    fun deleteNote(note: NoteEntity) {
        launchInBackground {
            noteDao.deleteNote(note)
        }
    }
}
