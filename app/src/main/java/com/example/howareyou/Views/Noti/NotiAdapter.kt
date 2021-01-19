package com.example.howareyou.views.noti

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.views.detail.DetailActivity
import com.example.howareyou.model.*
import com.example.howareyou.R
import com.example.howareyou.databinding.ItemHomePostingBinding
import com.example.howareyou.databinding.ItemNotificationBinding
import com.example.howareyou.util.ConvertTime
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.example.howareyou.views.home.CustomViewHolder
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.android.synthetic.main.item_notification.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import kotlin.collections.ArrayList

class NotiAdapter(val context: Context) : RecyclerView.Adapter<NotiViewHolder>(){

    var notiDTOList = ArrayList<NotiItem>()

    //클릭 인터페이스 정의
    interface ItemClickListener {
        fun onClick(view: View, position: Int, postArray: ArrayList<LoadPostItem>)
    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener

    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

    fun setItem(data: ArrayList<NotiItem>){
        this.notiDTOList.clear()
        this.notiDTOList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiViewHolder {

        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotiViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return notiDTOList.size
    }

    override fun onBindViewHolder(holder: NotiViewHolder, position: Int) {

        holder.onBind(notiDTOList[position])

        if(notiDTOList[position].viewed == false) holder.itemView.setBackgroundColor(ContextCompat.getColor(context,
            R.color.colorPrimaryLight
        ))
        else holder.itemView.setBackgroundColor(Color.WHITE)

        holder.itemView.notification_textview_title.text = notiDTOList[position].board.title
        holder.itemView.notification_textview_content.text = notiDTOList[position].content

        // 시간 convert
        val convtime = ConvertTime()
        holder.itemView.notification_textview_date.text = convtime.showTime(notiDTOList[position].createdAt)

        holder.itemView.setOnClickListener {
            // 이동
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("board_id",notiDTOList[position].board._id)
            context.startActivity(intent)

            // 갱신(viewed)
            //updateNotification(notiDTOList[position]._id)
            holder.itemView.setBackgroundColor(Color.WHITE)

            notifyDataSetChanged()
        }

    }

//    private fun updateNotification(noti_id : String) {
//        service = RetrofitClient.client!!.create(ServiceApi::class.java)
//
//        service?.updateNoti(noti_id, updateNotiDTO(true))?.enqueue(object : Callback<Void?> {
//            override fun onResponse(
//                call: Call<Void?>?,
//                response: Response<Void?>
//
//            ) {
//                if (response.isSuccessful) {
//                    //showProgress(false)
//                } else {
//                    // 실패시 resopnse.errorbody를 객체화
//                    val gson = Gson()
//                    val adapter: TypeAdapter<StatuscodeResponse> = gson.getAdapter<StatuscodeResponse>(
//                        StatuscodeResponse::class.java
//                    )
//                    try {
//                        if (response.errorBody() != null) {
//                            val result: StatuscodeResponse = adapter.fromJson(
//                                response.errorBody()!!.string()
//                            )
//
//                        }
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<Void?>?, t: Throwable) {
//                Log.e("onFailure", t.message!!)
//                //showProgress(false)
//            }
//        })
//
//    }
}

class NotiViewHolder(val binding : ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data : NotiItem?){
        binding.notiItem = data
    }
}