package com.example.glinda.ChatFragment

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.glinda.R
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_chat, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        return view
    }
    override fun onOptionsItemSelected(item: MenuItem):Boolean{
        if(item.itemId == R.id.action_myAcc) {
            findNavController().navigate(ChatFragmentDirections.actionChatFragmentToAccountFragment())
            return true
        }
        return true
    }











}
