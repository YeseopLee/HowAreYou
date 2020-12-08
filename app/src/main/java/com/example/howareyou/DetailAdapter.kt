package com.example.howareyou

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howareyou.Model.Comment
import com.example.howareyou.Model.PostLikedDTO
import com.example.howareyou.Model.PostingResponseDTO
import com.example.howareyou.Util.App
import com.example.howareyou.Util.OnSingleClickListener
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.view.*
import kotlinx.android.synthetic.main.activity_imageview_detail.view.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_recomment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailAdapter(val context: Context, val detailDTO: ArrayList<Comment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var service: ServiceApi? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType) {
            0 -> {
                var view = LayoutInflater.from(context).inflate(
                    R.layout.item_comment,
                    parent,
                    false
                )
                CommentViewHolder(view)
            }

            1 -> {
                var view = LayoutInflater.from(context).inflate(
                    R.layout.item_recomment,
                    parent,
                    false
                )
                ReCommentViewHolder(view)
            }

            else -> throw RuntimeException("에러")
        }
    }

    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class ReCommentViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return detailDTO.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (detailDTO[position].comment != null) {
            1
        }
        else {
            0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        // alert dialog value
        val builder = AlertDialog.Builder(context).create()

        // Get the LayoutInflater from Context
        val layoutInflater:LayoutInflater = LayoutInflater.from(context)

        // 댓글
        if (holder.itemViewType == 0){
            var view = holder.itemView
            view.comment_textview_content.text = detailDTO[position].content
            view.comment_textview_author.text = detailDTO[position].author
            view.comment_textview_date.text = detailDTO[position].createdAt

            // 좋아요
            view.comment_imageview_liked.setOnClickListener {
                postLiked(PostLikedDTO(App.prefs.myEmail, App.prefs.myId, null, detailDTO[position].id))
            }

            // 사용자 좋아요 상태 체크
            if(detailDTO[position].)
            for (i in 0 until) {
                if (result.likeds[i - 1].user_id == App.prefs.myId) {
                    detail_button_liked.setBackgroundResource(R.drawable.ic_thumbsup)
                }
            }

            //이미지
            var tempUrl = detailDTO[position].image?.formats?.thumbnail?.url
            var detailUrl = detailDTO[position].image?.url
            if(!tempUrl.isNullOrEmpty())
            {
                Glide.with(view).load(RetrofitClient.BASE_URL + tempUrl).into(view.comment_imageview_image)
                view.comment_imageview_image.visibility = View.VISIBLE
            }

            view.comment_imageview_image.setOnClickListener {

                val dialogView = layoutInflater.inflate(R.layout.activity_imageview_detail, null)
                Glide.with(dialogView).load(RetrofitClient.BASE_URL + detailUrl).into(dialogView.imageview_detail)
                builder.setView(dialogView)
                builder.show()

            }

            //더보기 버튼
            view.comment_button_morevert.setOnClickListener(object : OnSingleClickListener() {
                override fun onSingleClick(view: View) {

                    val dialogView = layoutInflater.inflate(R.layout.activity_more_menu, null)
                    val BtnReport = dialogView.findViewById<Button>(R.id.moremenu_button_report)
                    val BtnRecomment =
                        dialogView.findViewById<Button>(R.id.moremenu_button_recomment)

                    builder.setView(dialogView)
                    builder.show()

                    BtnReport.setOnClickListener {
                        builder.dismiss()
                    }
                    BtnRecomment.setOnClickListener {

                        App.prefs.tempCommentId = detailDTO[position].id
                        builder.dismiss()
                    }

                }
            })

        }
        else{ // 대댓글
            var view = holder.itemView
            view.recomment_textview_content.text = detailDTO[position].content
            view.recomment_textview_author.text = detailDTO[position].author
            view.recomment_textview_date.text = detailDTO[position].createdAt

            // 좋아요
            view.recomment_imageview_liked.setOnClickListener {
                postLiked(PostLikedDTO(App.prefs.myEmail, App.prefs.myId, null, detailDTO[position].id))
            }

            // 이미지
            var tempUrl = detailDTO[position].image?.formats?.thumbnail?.url
            var detailUrl = detailDTO[position].image?.url
            if(!tempUrl.isNullOrEmpty())
            {
                Glide.with(view).load(RetrofitClient.BASE_URL + tempUrl).into(view.recomment_imageview_image)
                view.recomment_imageview_image.visibility = View.VISIBLE
            }

            view.recomment_imageview_image.setOnClickListener {

                val dialogView = layoutInflater.inflate(R.layout.activity_imageview_detail, null)
                Glide.with(dialogView).load(RetrofitClient.BASE_URL + detailUrl).into(dialogView.imageview_detail)
                builder.setView(dialogView)
                builder.show()


            }

            //더보기 버튼
            view.recomment_button_morevert.setOnClickListener(object : OnSingleClickListener() {
                override fun onSingleClick(view: View) {

                    val dialogView = layoutInflater.inflate(R.layout.activity_more_menu, null)
                    val BtnReport = dialogView.findViewById<Button>(R.id.moremenu_button_report)
                    val BtnRecomment =
                        dialogView.findViewById<Button>(R.id.moremenu_button_recomment)
                    BtnRecomment.visibility = View.GONE

                    builder.setView(dialogView)
                    builder.show()

                    BtnReport.setOnClickListener {
                        builder.dismiss()
                    }

                }
            })
        }

    }

    private fun postLiked(data: PostLikedDTO) {
        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        service?.userLiked(data)?.enqueue(object : Callback<PostingResponseDTO?> {
            override fun onResponse(
                call: Call<PostingResponseDTO?>?,
                response: Response<PostingResponseDTO?>

            ) {
//                detail_button_liked.setBackgroundResource(R.drawable.ic_thumbsup)
//                detail_textview_liked.text = (detail_textview_liked.text.toString().toInt() + 1).toString()

            }

            override fun onFailure(call: Call<PostingResponseDTO?>?, t: Throwable) {
                Toast.makeText(context, "이미 좋아요를 눌렀습니다.", Toast.LENGTH_SHORT).show()
                Log.e("onFailure", t.message!!)
            }
        })

    }

}