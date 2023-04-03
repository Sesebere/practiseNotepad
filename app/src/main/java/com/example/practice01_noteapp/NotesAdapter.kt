package com.example.practice01_noteapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.practice01_noteapp.databinding.NoteLayoutBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NotesAdapter(var noteDb:AppDatabase ): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    //setting up viewBinding object
    private lateinit var noteList: MutableList<Note>

    var noteDao = noteDb!!.NoteDao()

    fun setAdapterList(noteList: MutableList<Note>){
        val oldList = noteList
        val diffResult:DiffUtil.DiffResult = DiffUtil.calculateDiff(
            NoteDiffCallback(
                oldList,
                noteList
            )
        )
        this.noteList = noteList
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapter.NoteViewHolder {
//        val binding = LayoutInflater.from(parent.context)
//            .inflate(R.layout.note_layout, parent, false)


        val binding = NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesAdapter.NoteViewHolder, position: Int) {
//        if(noteList.isEmpty()) return

        CurrentNote.note  = noteList.get(position)
        var currentNote = CurrentNote.note
        holder.noteTitle.text = currentNote.noteTitle
        holder.noteContent.text = currentNote.noteContent
        holder.editBtn.setOnClickListener {
//            val titleAndContentBundle = bundleOf("note_title" to holder.noteTitle.text.toString())
            val titleAndContentBundle = Bundle()
            titleAndContentBundle.putString("note_title", holder.noteTitle.text.toString())
            titleAndContentBundle.putString("note_content", holder.noteContent.text.toString())
            CurrentNote.note = noteList.get(position)
            it.findNavController().navigate(R.id.action_noteHome_to_noteScreen, titleAndContentBundle)
        }
        holder.deleteBtn.setOnClickListener {
            GlobalScope.launch {
                val deleteResult = GlobalScope.async {
                    noteDao.deleteNoteById(currentNote.noteId)
                }

                deleteResult.await() // Wait for the deletion to complete
                Log.i("noteTag","Deleted form Database")

                noteList.remove(currentNote)
                Log.i("noteTag", currentNote.toString())
            }
//            GlobalScope.launch{
//                noteDao.deleteNoteById(currentNote.noteId)
//                Log.i("noteTag","Deleted form Database")
//                noteList.remove(currentNote)
//                Log.i("noteTag", currentNote.toString())
//
//            }

            notifyItemRemoved(position)

        }


    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    class NoteViewHolder(binding: NoteLayoutBinding): RecyclerView.ViewHolder(binding.root){

        var noteTitle = binding.tvNoteTitle
        var noteContent = binding.tvNoteContent
        var editBtn = binding.btEdit
        var deleteBtn = binding.btDelete
    }

    class NoteDiffCallback(var oldNoteList: List<Note>, var newNoteList: List<Note>): DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldNoteList.size
        }

        override fun getNewListSize(): Int {
            return newNoteList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldNoteList.get(oldItemPosition).noteId == newNoteList.get(newItemPosition).noteId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldNoteList.get(oldItemPosition).equals(newNoteList.get(newItemPosition))
        }

    }
}
