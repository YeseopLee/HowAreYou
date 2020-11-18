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
import kotlinx.android.synthetic.main.item_recomment.view.*
import kotlin.collections.ArrayList

class DetailAdapter(val context: Context, val detailDTO : ArrayList<Comment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType) {
            0 -> {
                var view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false)
                CommentViewHolder(view)
            }

            1 -> {
                var view = LayoutInflater.from(context).inflate(R.layout.item_recomment,parent,false)
                ReCommentViewHolder(view)
            }

            else -> throw RuntimeException("에러")
        }

        //return CustomViewHolder(view)
    }

    inner class CommentViewHolder(view : View) : RecyclerView.ViewHolder(view)
    inner class ReCommentViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return detailDTO.size
    }

    override fun getItemViewType(position: Int): Int {
        System.out.println(detailDTO[position].comment+"커얼어ㅓ엉어")
        return if (detailDTO[position].comment != null) {
            1
        }
        else {
            0
        }

//        return commentDTO[position]
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == 0){
            var view = holder.itemView
            view.comment_textview_content.text = detailDTO[position].content
            view.comment_textview_author.text = detailDTO[position].author
        }
        else{
            var view = holder.itemView
            view.recomment_textview_content.text = detailDTO[position].content
            view.recomment_textview_author.text = detailDTO[position].author
        }


//        view.profile_layout.setOnClickListener {
//            val iT = Intent(context,ProfileActivity::class.java)
//            iT.putExtra("name",friendDTOfilter[position].name)
//            iT.putExtra("username",username)
//            context.startActivity(iT)
//        }

    }

}