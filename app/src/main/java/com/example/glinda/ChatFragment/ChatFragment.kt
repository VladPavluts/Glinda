package com.example.glinda.ChatFragment

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.glinda.R
import com.example.glinda.UsersAdapter
import com.example.glinda.util.FirestoreUtil
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {

    private lateinit var viewModel: ChatViewModel
    private  lateinit var adapter: UsersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        val view=inflater.inflate(R.layout.fragment_chat, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        val list=view.findViewById<RecyclerView>(R.id.personList)
        adapter = UsersAdapter(activity!!.applicationContext, emptyList())
        viewModel.getFromDb()
        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            adapter.users=users
            adapter.notifyDataSetChanged()
        })

        list.adapter=adapter
        list.layoutManager = LinearLayoutManager(context)

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
