package com.example.howareyou.views.Noti

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.views.Detail.DetailActivity
import com.example.howareyou.model.*
import com.example.howareyou.R
import com.example.howareyou.Util.ConvertTime
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.android.synthetic.main.item_notification.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import kotlin.collections.ArrayList

class NotiAdapter(val context: Context, val notiDTOList : ArrayList<NotiItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var service: ServiceApi? = null

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

        if(notiDTOList[position].viewed == false) view.setBackgroundColor(ContextCompat.getColor(context,
            R.color.colorPrimaryLight
        ))
        else view.setBackgroundColor(Color.WHITE)

        view.notification_textview_title.text = notiDTOList[position].board.title
        view.notification_textview_content.text = notiDTOList[position].content

        // 시간 convert
        val convtime = ConvertTime()
        view.notification_textview_date.text = convtime.showTime(notiDTOList[position].createdAt)

        view.setOnClickListener {
            // 이동
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("board_id",notiDTOList[position].board._id)
            context.startActivity(intent)

            // 갱신(viewed)
            updateNotification(notiDTOList[position]._id)
            view.setBackgroundColor(Color.WHITE)

            notifyDataSetChanged()
        }

    }

    private fun updateNotification(noti_id : String) {
        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        service?.updateNoti(noti_id, updateNotiDTO(true))?.enqueue(object : Callback<Void?> {
            override fun onResponse(
                call: Call<Void?>?,
                response: Response<Void?>

            ) {
                if (response.isSuccessful) {
                    //showProgress(false)
                } else {
                    // 실패시 resopnse.errorbody를 객체화
                    val gson = Gson()
                    val adapter: TypeAdapter<StatuscodeResponse> = gson.getAdapter<StatuscodeResponse>(
                        StatuscodeResponse::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            val result: StatuscodeResponse = adapter.fromJson(
                                response.errorBody()!!.string()
                            )

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<Void?>?, t: Throwable) {
                Log.e("onFailure", t.message!!)
                //showProgress(false)
            }
        })

    }
}