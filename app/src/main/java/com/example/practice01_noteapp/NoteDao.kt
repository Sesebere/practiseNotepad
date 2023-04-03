package com.example.practice01_noteapp

import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    suspend fun insertNotes(vararg notes: Note)

    @Update
    suspend fun updateNotes(vararg notes: Note)

    @Query("UPDATE tbl_notes SET note_title = :noteTitle, note_content = :noteContent WHERE note_id = :noteId")
    suspend fun updateNote(noteId:Int, noteTitle:String, noteContent:String)

    @Delete
    suspend fun deleteNotes(vararg notes: Note)

    @Query("DELETE FROM tbl_notes WHERE note_id = :id")
    suspend fun deleteNoteById(id: Int)

    @Query("SELECT * FROM tbl_notes")
    suspend fun getAllNotes(): Array<Note>

}