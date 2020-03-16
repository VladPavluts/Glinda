package com.example.glinda

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.glinda.model.TextMessage
import com.google.firebase.auth.FirebaseAuth
import java.text.DateFormat


class MessageAdapter (val context: Context,var messages: List<TextMessage>): RecyclerView.Adapter<MessageAdapter.ViewHolder>(){

    private  val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemCount(): Int = messages.size
    private fun getItem(position: Int)= messages[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            inflater.inflate(R.layout.item_message, parent, false),
            context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View, val context: Context): RecyclerView.ViewHolder(itemView) {

        private val messageText: TextView = itemView.findViewById(R.id.textView_message_text)
        private val messageTime: TextView = itemView.findViewById(R.id.textView_message_time)
       private val messageRoot: RelativeLayout = itemView.findViewById(R.id.message_root)


        fun bind(message: TextMessage){
            messageText.text=message.text
            setTimeText(message)
            setMessageRootGravity(message)

        }
        private fun setTimeText(message: TextMessage){
            val dateFormat=DateFormat.getDateTimeInstance()
            messageTime.text=dateFormat.format(message.time)
        }
        private fun setMessageRootGravity(message: TextMessage){
            if(message.senderId == FirebaseAuth.getInstance().currentUser?.uid){
                messageRoot.apply{
                    val lParams=FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,Gravity.END)
                    this.layoutParams=lParams

                }
            }
            else{
                messageRoot.apply{
                    val lParams=FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,Gravity.START)
                    this.layoutParams=lParams

                }
            }
        }
    }
}