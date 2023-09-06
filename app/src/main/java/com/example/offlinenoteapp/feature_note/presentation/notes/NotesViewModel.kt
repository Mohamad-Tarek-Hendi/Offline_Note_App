package com.example.offlinenoteapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offlinenoteapp.feature_note.domain.model.Note
import com.example.offlinenoteapp.feature_note.domain.use_cases.base.NotesUseCases
import com.example.offlinenoteapp.feature_note.domain.util.NoteOrder
import com.example.offlinenoteapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val notesUseCases: NotesUseCases) :
    ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(notesEvent: NotesEvent) {

        when (notesEvent) {

            is NotesEvent.Order -> {
                if (_state.value.noteOrder::class == notesEvent.noteOrder::class
                    && _state.value.noteOrder.orderType == notesEvent.noteOrder.orderType
                ) {
                    return
                }

                getNotes(notesEvent.noteOrder)
            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    notesUseCases.deleteNoteUseCase(notesEvent.note)
                    recentlyDeletedNote = notesEvent.note
                }
            }

            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    notesUseCases.upsertNoteUseCase(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !_state.value.isOrderSectionVisible
                )
            }
        }

    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        // Note every time we call this function we
        // get new instance from that flow so we should cancel old coroutine -> using Job
        getNotesJob = notesUseCases.getNotesUseCase(noteOrder).onEach { notes ->
            _state.value = state.value.copy(
                notes = notes,
                noteOrder = noteOrder
            )
        }.launchIn(viewModelScope)
    }
}