package com.example.howareyou

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.Model.CommentDTO
import com.example.howareyou.Model.PostingDTO
import kotlinx.android.synthetic.main.item_comment.view.*
import java.util.*
import kotlin.collections.ArrayList

class DetailAdapter(val context: Context, val commentDTO : ArrayList<CommentDTO>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType) {
            0 -> {
                var view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false)
                CustomViewHolder(view)
            }
            else -> throw RuntimeException("에러")
        }

        //return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return commentDTO.size
    }

    override fun getItemViewType(position: Int): Int {
        return commentDTO[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var view = holder.itemView
        view.comment_textview.text = commentDTO[position].content


//        view.profile_layout.setOnClickListener {
//            val iT = Intent(context,ProfileActivity::class.java)
//            iT.putExtra("name",friendDTOfilter[position].name)
//            iT.putExtra("username",username)
//            context.startActivity(iT)
//        }

    }

}