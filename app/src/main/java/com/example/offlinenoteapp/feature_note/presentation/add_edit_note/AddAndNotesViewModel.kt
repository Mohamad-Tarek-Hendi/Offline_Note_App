package com.example.offlinenoteapp.feature_note.presentation.add_edit_note

import androidx.lifecycle.ViewModel
import com.example.offlinenoteapp.feature_note.domain.use_cases.base.NotesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddAndNotesViewModel @Inject constructor(private val notesUseCases: NotesUseCases) :
    ViewModel() {
}