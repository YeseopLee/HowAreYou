package com.example.howareyou.views.Writing

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howareyou.R
import kotlinx.android.synthetic.main.item_imageupload.view.*
import kotlin.collections.ArrayList

class WritingAdapter(val context: Context, val uriList : ArrayList<Uri>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val TAG : String = "WritingAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.item_imageupload,parent,false)
        return ImageViewHolder(view)

    }

    inner class ImageViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return uriList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var view = holder.itemView
        Glide.with(view).load(uriList[position]).into(view.imageupload_imageview)

        view.imageupload_button_close.setOnClickListener {
            //리스트에서 해당 요소 제거
            uriList.remove(uriList[position])
            notifyDataSetChanged()
        }

        Log.d(TAG,uriList.toString())
    }

}