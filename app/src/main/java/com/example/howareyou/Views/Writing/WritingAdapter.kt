package com.example.howareyou.views.writing

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howareyou.databinding.ItemHomePostingBinding
import com.example.howareyou.databinding.ItemImageuploadBinding
import com.example.howareyou.databinding.ItemNotificationBinding
import kotlinx.android.synthetic.main.item_imageupload.view.*
import kotlin.collections.ArrayList

class WritingAdapter(val context: Context) : RecyclerView.Adapter<WritingViewHolder>(){

    val uriList = ArrayList<Uri>()
    val TAG : String = "WritingAdapter"

    //클릭 인터페이스 정의
    interface ItemClickListener {
        fun onClick(view: View, position: Int, postArray: ArrayList<String>)
    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener

    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

    fun setItem(data: ArrayList<Uri>){
        this.uriList.clear()
        this.uriList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WritingViewHolder {

        val binding = ItemImageuploadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WritingViewHolder(binding)

    }


    override fun getItemCount(): Int {
        return uriList.size
    }

    override fun onBindViewHolder(holder: WritingViewHolder, position: Int) {

        Log.e("adapterlist",uriList.toString())

        holder.onBind(uriList[position])

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

class WritingViewHolder(val binding : ItemImageuploadBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data : Uri?){
        binding.imageItem = data
    }
}