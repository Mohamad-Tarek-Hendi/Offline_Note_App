package com.example.offlinenoteapp.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.offlinenoteapp.core.ui.theme.BabyBlue
import com.example.offlinenoteapp.core.ui.theme.LightGreen
import com.example.offlinenoteapp.core.ui.theme.RedOrange
import com.example.offlinenoteapp.core.ui.theme.RedPink
import com.example.offlinenoteapp.core.ui.theme.Violet

@Entity
data class Note(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int
) {
    companion object {
        val noteColor = listOf(
            RedOrange,
            RedPink,
            BabyBlue,
            Violet,
            LightGreen
        )
    }
}


class InvalidNoteException(message: String) : Exception(message)