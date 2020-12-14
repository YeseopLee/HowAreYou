package com.example.howareyou

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howareyou.Model.Comment
import com.example.howareyou.Model.ImageDTO
import com.example.howareyou.Util.App
import com.example.howareyou.Util.OnSingleClickListener
import com.example.howareyou.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_imageview_detail.view.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_imageshow.view.*
import kotlinx.android.synthetic.main.item_imageupload.view.*
import kotlinx.android.synthetic.main.item_recomment.view.*
import kotlin.collections.ArrayList

class DetailImageAdapter(val context: Context, val uriList : ArrayList<ImageDTO>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val TAG : String = "Detail_imageAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.item_imageshow,parent,false)
        return ImageViewHolder(view)

    }

    inner class ImageViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return uriList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        // alert dialog value
        val builder = AlertDialog.Builder(context).create()

        // Get the LayoutInflater from Context
        val layoutInflater:LayoutInflater = LayoutInflater.from(context)

        var view = holder.itemView
        Glide.with(view).load(uriList[position].thumbnail).into(view.imageshow_imageview)

        var detailUrl = uriList[position].image

        System.out.println("test"+detailUrl)

        view.imageshow_imageview.setOnClickListener {

            val dialogView = layoutInflater.inflate(R.layout.activity_imageview_detail, null)
            Glide.with(dialogView).load(detailUrl).into(dialogView.imageview_detail)
            builder.setView(dialogView)
            builder.show()

        }

    }

}