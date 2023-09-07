package com.example.offlinenoteapp.feature_note.domain.use_cases.base

import com.example.offlinenoteapp.feature_note.domain.use_cases.DeleteNoteUseCase
import com.example.offlinenoteapp.feature_note.domain.use_cases.GetNoteByIdUseCase
import com.example.offlinenoteapp.feature_note.domain.use_cases.GetNotesUseCase
import com.example.offlinenoteapp.feature_note.domain.use_cases.UpsertNoteUseCase

data class NotesUseCases(
    val getNotesUseCase: GetNotesUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val upsertNoteUseCase: UpsertNoteUseCase,
    val getNoteByIdUseCase: GetNoteByIdUseCase
)
