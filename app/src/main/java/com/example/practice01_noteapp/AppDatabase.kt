package com.example.practice01_noteapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun NoteDao(): NoteDao

    companion object {
        private var INSTANCE:AppDatabase? = null

        fun getDatabaseInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                createDatabase(context)
            }

            return INSTANCE
        }

        private fun createDatabase(context: Context) {
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "note_database"
                ).build()
            }
        }
    }
}