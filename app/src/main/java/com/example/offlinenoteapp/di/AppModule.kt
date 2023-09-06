package com.example.offlinenoteapp.di

import android.app.Application
import androidx.room.Room
import com.example.offlinenoteapp.feature_note.data.data_source.NoteDatabase
import com.example.offlinenoteapp.feature_note.data.data_source.NoteDatabase.Companion.DATABASE_NAME
import com.example.offlinenoteapp.feature_note.data.repository.NoteRepositoryImp
import com.example.offlinenoteapp.feature_note.domain.repository.NoteRepository
import com.example.offlinenoteapp.feature_note.domain.use_cases.DeleteNoteUseCase
import com.example.offlinenoteapp.feature_note.domain.use_cases.GetNotesUseCase
import com.example.offlinenoteapp.feature_note.domain.use_cases.UpsertNoteUseCase
import com.example.offlinenoteapp.feature_note.domain.use_cases.base.NotesUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Note : This module is responsible for providing dependencies for various components in the application. **/
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /** providing a singleton instance of the NoteDatabase **/
    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    /** provides a singleton instance of the NoteRepository **/
    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImp(db.noteDao)
    }

    /** provides a singleton instance of the NoteCases class, which encapsulates the use cases related to notes **/
    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NotesUseCases {
        return NotesUseCases(
            getNotesUseCase = GetNotesUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository),
            upsertNoteUseCase = UpsertNoteUseCase(repository)
        )
    }

}