package com.example.glinda

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.glinda.glide.GlideApp
import com.example.glinda.model.User
import com.example.glinda.util.StorageUtil

class UsersAdapter(val context :Context,var users:List<User>) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>(){

    private  val inflater: LayoutInflater= LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_person, parent,false),context)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View,val context: Context): RecyclerView.ViewHolder(itemView) {
        private val photo: ImageView =itemView.findViewById(R.id.image_id)
        private val personName: TextView =itemView.findViewById(R.id.personName)
        private val personBio: TextView =itemView.findViewById(R.id.personBio)

        /*init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener(position)
                }
            }
        }*/

        fun bind(user: User){
            if(user.profilePicturePath!=null){
                val options= RequestOptions().circleCrop()
                GlideApp.with(context)
                    .load(StorageUtil.getCurrentRef(user.profilePicturePath))
                    .apply(options)
                    .placeholder(R.drawable.ic_account_circle_biriz_24dp)
                    .into(photo)
            }
            personName.text=user.name
            personBio.text=user.bio
        }
    }

    override fun getItemCount(): Int = users.size
    private fun getItem(position: Int)= users[position]


}