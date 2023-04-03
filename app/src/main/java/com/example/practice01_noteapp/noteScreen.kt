package com.example.practice01_noteapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.practice01_noteapp.databinding.FragmentNoteScreenBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [noteScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class noteScreen : Fragment() {
    private lateinit var binding: FragmentNoteScreenBinding
    private var noteTitle = ""
    private var noteContent =""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        //setting note title and note content
        noteTitle = arguments?.getString("note_title").toString()
        noteContent = arguments?.getString("note_content").toString()
        binding.etNoteTitle.setText(noteTitle)
        binding.etmNoteContent.setText(noteContent)
        Log.i("noteTag","I am the id: ${CurrentNote.note.noteId}")
        Log.i("noteTag","entire noteList: ${Home.noteList}")
        return view
    }

    override fun onDestroy() {
        var db = AppDatabase.getDatabaseInstance(requireActivity())
        var noteDao = db!!.NoteDao()
        GlobalScope.launch {
            //updating the note in the database
            noteDao.updateNote(CurrentNote.note.noteId,
                binding.etNoteTitle.text.toString(),
                binding.etmNoteContent.text.toString())
            //updating the note in noteList
            Home.noteList.get(Home.noteList.indexOf(CurrentNote.note)).noteTitle =  binding.etNoteTitle.text.toString()

            Home.noteList.get(Home.noteList.indexOf(CurrentNote.note)).noteContent =  binding.etmNoteContent.text.toString()
            Log.i("noteTAG", "noteUpdated")
        }

//        Home.noteList.add(Note(0,"aaaaaaaaa","bbbbbbbbbbbbbb"))
        super.onDestroy()
//        Fragment.fin
    }


    }
