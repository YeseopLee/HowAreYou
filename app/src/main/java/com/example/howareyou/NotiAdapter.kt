package com.example.howareyou

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.Model.*
import com.example.howareyou.Util.App
import kotlinx.android.synthetic.main.item_home_posting.view.*
import kotlinx.android.synthetic.main.item_notification.view.*
import kotlinx.android.synthetic.main.item_posting.view.*
import kotlinx.android.synthetic.main.item_posting.view.posting_textview_title
import java.util.*
import kotlin.collections.ArrayList

class NotiAdapter(val context: Context, val notiDTOList : ArrayList<NotiItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType) {
            0 -> {
                var view = LayoutInflater.from(context).inflate(R.layout.item_notification,parent,false)
                CustomViewHolder(view)
            }
            else -> throw RuntimeException("에러")
        }

        //return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return notiDTOList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var view = holder.itemView
        view.notification_textview_title.text = notiDTOList[position].board.title
        view.notification_textview_content.text = notiDTOList[position].content
        view.notification_textview_date.text = notiDTOList[position].createdAt

    }
}