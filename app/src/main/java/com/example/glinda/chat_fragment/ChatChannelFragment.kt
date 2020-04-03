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
import com.example.glinda.adapters.MessageAdapter
import com.example.glinda.R
import com.example.glinda.model.TextMessage
import com.example.glinda.model.User
import com.example.glinda.util.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_chat_channel.view.*
import java.util.*


class ChatChannelFragment : Fragment() {

    private lateinit var viewModel: ChatChannelViewModel
    private lateinit var adapter: MessageAdapter
    private lateinit var currentUser: User
    private lateinit var user_id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_channel, container, false)
        val list1 = view.findViewById<RecyclerView>(R.id.messageList)
        val user_name=arguments!!.getString("USER_NAME")
        user_id = arguments!!.getString("USER_ID")!!

        FirestoreUtil.getCurrentUser { currentUser=it }

        viewModel = ViewModelProvider(this).get(ChatChannelViewModel::class.java)
        viewModel.getFromMessagCloud(user_id)

        adapter = MessageAdapter(
            activity!!.applicationContext,
            emptyList<TextMessage>().toMutableList()
        )

        viewModel.messages.observe(viewLifecycleOwner, Observer { messages ->
            adapter.updateRecyclerView(messages)
            adapter.notifyDataSetChanged()
        })

        view.apply {
            image_send.setOnClickListener {
                val messageToSend = TextMessage(
                    inputMessage.text.toString(), Calendar.getInstance().time,
                    FirebaseAuth.getInstance().currentUser!!.uid, user_id,currentUser.name
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
            findViewById<Toolbar>(R.id.toolbarChatChannel).title=user_name
        }

        val layoutManager=LinearLayoutManager(context)
        layoutManager.stackFromEnd = true
        list1.adapter=adapter
        list1.layoutManager = layoutManager

        return view

    }

}
