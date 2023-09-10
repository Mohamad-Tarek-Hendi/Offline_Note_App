package com.example.offlinenoteapp.feature_note.presentation.add_edit_note

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offlinenoteapp.feature_note.domain.model.InvalidNoteException
import com.example.offlinenoteapp.feature_note.domain.model.Note
import com.example.offlinenoteapp.feature_note.domain.use_cases.base.NotesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNotesViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter Title..."))
    val noteTitle: State<NoteTextFieldState> = _noteTitle
    val detectTitle: State<NoteTextFieldState> = _noteTitle


    private val _noteContent = mutableStateOf(NoteTextFieldState(hint = "Enter Content..."))
    val noteContent: State<NoteTextFieldState> = _noteContent
    val detectContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColor.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->

            // Detect if user click to note or floating button
            if (noteId != -1) {
                viewModelScope.launch {
                    notesUseCases.getNoteByIdUseCase(noteId)?.also { note ->
                        currentNoteId = noteId

                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )

                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )

                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {

        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focus.isFocused && noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focus.isFocused && noteContent.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }

            is AddEditNoteEvent.SaveNote -> {

                viewModelScope.launch {
                    try {
                        if (!detectTitle.value.isHintVisible && !detectContent.value.isHintVisible) {
                            notesUseCases.upsertNoteUseCase(
                                Note(
                                    id = currentNoteId,
                                    title = noteTitle.value.text,
                                    content = noteContent.value.text,
                                    timestamp = System.currentTimeMillis(),
                                    color = noteColor.value
                                )
                            )
                            _eventFlow.emit(UiEvent.SaveNote)
                        } else {
                            _eventFlow.emit(UiEvent.SaveNoteNotComplete)
                        }

                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }

            }
        }

    }
}