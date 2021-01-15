package com.example.howareyou.views.detail

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howareyou.model.ImageDTO
import com.example.howareyou.R
import com.example.howareyou.databinding.ItemHomePostingBinding
import com.example.howareyou.databinding.ItemImageshowBinding
import com.example.howareyou.model.Comment
import com.example.howareyou.model.LoadPostItem
import com.example.howareyou.views.home.CustomViewHolder
import kotlinx.android.synthetic.main.activity_imageview_detail.view.*
import kotlinx.android.synthetic.main.item_imageshow.view.*
import kotlin.collections.ArrayList

class DetailImageAdapter(val context: Context) : RecyclerView.Adapter<CustomViewHolder2>(){

    var uriList = ArrayList<ImageDTO>()

    fun setItem(data: ArrayList<ImageDTO>){
        this.uriList.clear()
        this.uriList.addAll(data)
        Log.e("urilist",uriList.toString())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder2 {

        var binding = ItemImageshowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder2(binding)

    }

    override fun getItemCount(): Int {
        Log.e("imageTest",uriList.size.toString())
        return uriList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder2, position: Int) {

        // alert dialog value
        val builder = AlertDialog.Builder(context).create()

        // Get the LayoutInflater from Context
        val layoutInflater:LayoutInflater = LayoutInflater.from(context)

        holder.onBind(uriList[position].thumbnail)

        Log.e("imageTes01",uriList[position].thumbnail)

//        var view = holder.itemView
//        Glide.with(view).load(uriList[position].thumbnail).into(view.imageshow_imageview)

        var detailUrl = uriList[position].image

        holder.itemView.imageshow_imageview.setOnClickListener {

            val dialogView = layoutInflater.inflate(R.layout.activity_imageview_detail, null)
            Glide.with(dialogView).load(detailUrl).into(dialogView.imageview_detail)
            builder.setView(dialogView)
            builder.show()

        }

    }

}

class CustomViewHolder2(val binding : ItemImageshowBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data : String){
        binding.imageItem = data
    }
}

