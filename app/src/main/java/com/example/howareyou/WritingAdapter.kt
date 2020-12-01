package com.example.howareyou

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.Model.Comment
import com.example.howareyou.Model.ImageDTO
import com.example.howareyou.Util.App
import com.example.howareyou.Util.OnSingleClickListener
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_imageupload.view.*
import kotlinx.android.synthetic.main.item_recomment.view.*
import kotlin.collections.ArrayList

class WritingAdapter(val context: Context, val imageDTO : ArrayList<ImageDTO>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.item_imageupload,parent,false)
        return ImageViewHolder(view)

    }

    inner class ImageViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return imageDTO.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var view = holder.itemView
        view.comment_textview_content.text = imageDTO[position].content

        view.imageupload_button_close.setOnClickListener {
            //TODO 리스트에서 해당 요소 제거?
        }
    }

}