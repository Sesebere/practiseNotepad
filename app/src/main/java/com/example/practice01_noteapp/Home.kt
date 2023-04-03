package com.example.practice01_noteapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.practice01_noteapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Boolean.getBoolean

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    private var shouldLoadRecyclerViewAndNotes = true

    companion object {
        var noteList: MutableList<Note> = mutableListOf()
    }

    private lateinit var binding: FragmentHomeBinding
    private var searchedNoteList: MutableList<Note> = mutableListOf()
    private lateinit var adapter: NotesAdapter

//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        var view = binding.root
//        return inflater.inflate(R.layout.fragment_home, container, false)
//        var note1 = Note(
//            0, "note1", "Lorem ipsum dolor sit amet, " +
//                    "consectetur adipisci elit," +
//                    " 1111111111111111111111111111111111111111111111111111111111" +
//                    " aliqua. Ut enim ad minim veniam, quis nostrum exercitationem " +
//                    "ullam corporis suscipit laboriosam, nisi ut aliquid ex ea " +
//                    "commodi consequatur. Quis aute iure reprehenderit."
//        )
//        var note2 = Note(
//            0, "note2", "Lorem ipsum dolor sit amet, " +
//                    "consectetur adipisci elit," +
//                    " s2222222222222222222222222222222222222222222222222222222222" +
//                    " aliqua. Ut enim ad minim veniam, quis nostrum exercitationem " +
//                    "ullam corporis suscipit laboriosam, nisi ut aliquid ex ea " +
//                    "commodi consequatur. Quis aute iure reprehenderit."
//        )
//        var note3 = Note(
//            0, "note3", "Lorem ipsum dolor sit amet, " +
//                    "consectetur adipisci elit," +
//                    "33333333333333333333333333333333333333333333333333333333333333" +
//                    " aliqua. Ut enim ad minim veniam, quis nostrum exercitationem " +
//                    "uffbllam corporis suscipit laboriosam, nisi ut aliquid ex ea " +
//                    "commodi consequatur. Quis aute iure reprehenderit."
//        )
//        noteList.add(note1)
//        noteList.add(note2)
//        noteList.add(note3)

        //DATABASE STUFF
        var noteDb = AppDatabase.getDatabaseInstance(requireContext())
        var noteDao = noteDb!!.NoteDao()
        //inserting the notes into the database and populating the notelist with the inserted values
//        runBlocking {
        GlobalScope.launch {
//                noteDao.insertNotes(note1, note2, note3)
//                shouldLoadRecyclerViewAndNotes = getBoolean("shouldLoadRecyclerViewAndNotes")
//                if (shouldLoadRecyclerViewAndNotes) {
            if (LoadDecisions.shouldLoadNotes) {
//                    noteList = mutableListOf()
                noteDao.getAllNotes().forEach { noteList.add(it) }
//                    shouldLoadRecyclerViewAndNotes = false
                LoadDecisions.shouldLoadNotes = false
                Log.i("noteTag", "shouldLoadRecyclerViewAndNotes is false")

            }
        }
//        }
        var recyclerView = binding.rvNotes


//        var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?

        adapter = NotesAdapter(noteDb)
        recyclerView.adapter = adapter
        adapter.setAdapterList(noteList)
        adapter.notifyDataSetChanged()


        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
//        adapter.notifyDataSetChanged()

        //FLOATING ACTION BUTTON
        binding.fabAddNote.setOnClickListener {
            it.findNavController().navigate(R.id.action_noteHome_to_addNoteFragment)
        }

        //Search View Submit Button
        binding.svSearch.isSubmitButtonEnabled = true

        performSearch()

        return view


    }
    private fun performSearch() {
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                Log.i("NoteTag", "onQueryTextChange called")
                return true
            }

        })
    }

    private fun search(query: String?) {
        searchedNoteList.clear()
        //Of the length of the qurey is 0, all the notes in noteList will be added to searchedNoteList; so instead of
        //copying all the objects from noteList to searchedNoteList and risking extreme memory consumption, I just pass
        //noteLis to the updateRecyclerView method straight away and exit the search methond
        if(query!!.length == 0){
            updateRecyclerView(adapter, noteList)
            return
        }

        query?.let {
            for (note in noteList) {
                if (note.noteTitle.contains(query, true)) {
                    searchedNoteList.add(note)
                }
            }
            if (searchedNoteList.isEmpty()) {
                Log.i("noteTag","searchedNoteLIst is empty")
                Toast.makeText(
                    requireContext(), "No such Title exists",
                    Toast.LENGTH_SHORT
                ).show()
            }
//            return

        }
        updateRecyclerView(adapter, searchedNoteList)
    }

    private fun updateRecyclerView(adapter: NotesAdapter, listToUSe: MutableList<Note>) {
        adapter.setAdapterList(listToUSe)
        adapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("shouldLoadRecyclerViewAndNotes", shouldLoadRecyclerViewAndNotes)
    }
}