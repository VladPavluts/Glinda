package com.example.glinda.ChatFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.glinda.MessageAdapter
import com.example.glinda.R
import com.example.glinda.model.MessageType
import com.example.glinda.model.TextMessage
import com.example.glinda.model.User
import com.example.glinda.util.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_chat_channel.view.*
import java.util.*


class ChatChannelFragment : Fragment() {

    private lateinit var viewModel: ChatChannelViewModel
    private lateinit var adapter: MessageAdapter
    private lateinit var chan: String

    private fun set(){
        adapter.notifyDataSetChanged()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_channel, container, false)
        val user: User = arguments!!.getParcelable("USER")!!
        val user_id: String = arguments!!.getString("USER_ID")!!

        viewModel = ViewModelProvider(this).get(ChatChannelViewModel::class.java)

        val list1 = view.findViewById<RecyclerView>(R.id.messageList)
        adapter = MessageAdapter(activity!!.applicationContext, emptyList())

        viewModel.getFromMessagCloud(user_id)

        viewModel.messages.observe(viewLifecycleOwner, Observer { messages ->
            adapter.messages = messages
            adapter.notifyDataSetChanged()
        })
        viewModel.channelId.observe(viewLifecycleOwner, Observer {
            chan = it
        })


        view.apply {
            image_send.setOnClickListener {
                val messageToSend = TextMessage(
                    inputMessage.text.toString(), Calendar.getInstance().time,
                    FirebaseAuth.getInstance().currentUser!!.uid, MessageType.TEXT
                )
                inputMessage.setText("")
                FirestoreUtil.sendMessage(messageToSend, chan)
                set()
            }
        }
        val toolbar = view.findViewById<Toolbar>(R.id.toolbarChatChannel)
        toolbar.title = user.name

        list1.adapter=adapter
        list1.layoutManager = LinearLayoutManager(context)
        return view

    }
}
