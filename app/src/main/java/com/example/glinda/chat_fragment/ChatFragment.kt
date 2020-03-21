package com.example.glinda.chat_fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.glinda.Const.KEYPOS
import com.example.glinda.Const.USER
import com.example.glinda.Const.USER_ID
import com.example.glinda.R
import com.example.glinda.UsersAdapter

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
        adapter = UsersAdapter(activity!!.applicationContext, emptyList()){
            position ->
            val users = viewModel.users.value!!
            val user=users[position]
            val bundle=Bundle()
            bundle.putInt(KEYPOS,position)
            bundle.putString(USER_ID,user.userID)
            bundle.putParcelable(USER,user.user)

            findNavController().navigate(R.id.chatChannelFragment,bundle)

        }
        viewModel.getFromCloudSt()

        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            adapter.userItems=users
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
