package com.example.glinda.chat_fragment

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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_chat_channel.view.*
import java.util.*


class ChatChannelFragment : Fragment() {

    private lateinit var viewModel: ChatChannelViewModel
    private lateinit var adapter: MessageAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_channel, container, false)
        val user: User = arguments!!.getParcelable("USER")!!
        val user_id: String = arguments!!.getString("USER_ID")!!

        val list1 = view.findViewById<RecyclerView>(R.id.messageList)
        viewModel = ViewModelProvider(this).get(ChatChannelViewModel::class.java)


        adapter = MessageAdapter(activity!!.applicationContext, emptyList<TextMessage>().toMutableList())

        viewModel.getFromMessagCloud(user_id)

        viewModel.messages.observe(viewLifecycleOwner, Observer { messages ->
            adapter.updateRecyclerView(messages,view)
            adapter.notifyDataSetChanged()
        })

        view.apply {
            image_send.setOnClickListener {
                val messageToSend = TextMessage(
                    inputMessage.text.toString(), Calendar.getInstance().time,
                    FirebaseAuth.getInstance().currentUser!!.uid, MessageType.TEXT
                )
                inputMessage.setText("")
                if(messageToSend.text!=""){
                    viewModel.sendMessage(messageToSend)
                    viewModel.getFromMessagCloud(user_id)
                    adapter.updateItemRV()
                }
                list1.postDelayed({
                    if((adapter.itemCount-1)!=-1)
                        list1.smoothScrollToPosition(adapter.itemCount-1)
                },100)
            }
            findViewById<Toolbar>(R.id.toolbarChatChannel).title=user.name
        }


        val layoutManager=LinearLayoutManager(context)
        layoutManager.stackFromEnd = true
        list1.adapter=adapter
        list1.layoutManager = layoutManager

        return view

    }

}
