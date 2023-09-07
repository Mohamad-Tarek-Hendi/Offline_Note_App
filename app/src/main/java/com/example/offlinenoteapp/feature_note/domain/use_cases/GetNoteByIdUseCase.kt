package com.example.offlinenoteapp.feature_note.domain.use_cases

import com.example.offlinenoteapp.feature_note.domain.model.Note
import com.example.offlinenoteapp.feature_note.domain.repository.NoteRepository

class GetNoteByIdUseCase(private val repository: NoteRepository) {

    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}