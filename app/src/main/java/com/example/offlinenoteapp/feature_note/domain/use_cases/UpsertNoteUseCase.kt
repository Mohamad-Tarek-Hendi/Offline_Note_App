package com.example.offlinenoteapp.feature_note.domain.use_cases

import com.example.offlinenoteapp.feature_note.domain.model.InvalidNoteException
import com.example.offlinenoteapp.feature_note.domain.model.Note
import com.example.offlinenoteapp.feature_note.domain.repository.NoteRepository

class UpsertNoteUseCase(private val repository: NoteRepository) {

    suspend operator fun invoke(note: Note) {

        @Throws(InvalidNoteException::class)
        if (note.title.isBlank()) {
            throw InvalidNoteException("The title of the note can't be empty..")
        }

        @Throws(InvalidNoteException::class)
        if (note.content.isBlank()) {
            throw InvalidNoteException("The content of the note can't be empty..")
        }
        repository.upsertNote(note)
    }
}