package com.example.howareyou

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.Model.Comment
import com.example.howareyou.Model.CommentDTO
import com.example.howareyou.Model.LoadPostItem
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlin.collections.ArrayList

class DetailAdapter(val context: Context, val detailDTO : ArrayList<Comment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


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
        return detailDTO.size
    }

//    override fun getItemViewType(position: Int): Int {
//        return commentDTO[position]
//    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var view = holder.itemView
        view.comment_textview_content.text = detailDTO[position].content
        view.comment_textview_author.text = detailDTO[position].author
//        view.comment_textview_liked.text = detailDTO[position].comments!![0].


//        view.profile_layout.setOnClickListener {
//            val iT = Intent(context,ProfileActivity::class.java)
//            iT.putExtra("name",friendDTOfilter[position].name)
//            iT.putExtra("username",username)
//            context.startActivity(iT)
//        }

    }

}