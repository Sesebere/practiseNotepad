package com.example.practice01_noteapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    var noteId: Int,
    @ColumnInfo(name = "note_title")
    var noteTitle: String,
    @ColumnInfo(name = "note_content")
    var noteContent: String
//    @ColumnInfo(name = "note_color")
//    var noteColor: String

) {
    override fun equals(other: Any?): Boolean {
        if(javaClass != other?.javaClass) return false
        other as Note
        if(noteId != other.noteId) return false
        if(noteTitle != other.noteTitle) return false
        if(noteContent != other.noteContent) return false
        return true
    }
}