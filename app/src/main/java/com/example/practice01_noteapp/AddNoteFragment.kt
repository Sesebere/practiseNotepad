package com.example.practice01_noteapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.practice01_noteapp.databinding.FragmentAddNoteBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddNoteFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        var view = binding.root
        // Inflate the layout for this fragment
        binding.btSaveNote.setOnClickListener {
            var db = AppDatabase.getDatabaseInstance(requireActivity())
            var noteDao = db!!.NoteDao()
            GlobalScope.launch {
                noteDao.insertNotes(Note(0, binding.etNoteTitle.text.toString(), binding.etmNoteContent.text.toString()))
                Home.noteList.add(Note(0, binding.etNoteTitle.text.toString(), binding.etmNoteContent.text.toString()))
            }

            var fragmentTransaction = getFragmentManager()?.beginTransaction()
            fragmentTransaction?.remove(this)
            fragmentTransaction?.commit()


            it.findNavController().navigate(R.id.action_addNoteFragment_to_noteHome)
//            super.onDestroy()
        }
        return view
    }



}