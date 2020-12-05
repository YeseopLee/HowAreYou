package com.example.howareyou

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howareyou.Model.Comment
import com.example.howareyou.Util.App
import com.example.howareyou.Util.OnSingleClickListener
import com.example.howareyou.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_imageview_detail.view.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_recomment.view.*


class DetailAdapter(val context: Context, val detailDTO: ArrayList<Comment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


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
                        System.out.println(detailDTO[position].id);
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

            var tempUrl = detailDTO[position].image?.formats?.thumbnail?.url
            if(!tempUrl.isNullOrEmpty())
            {
                Glide.with(view).load(RetrofitClient.BASE_URL + tempUrl).into(view.recomment_imageview_image)
                view.recomment_imageview_image.visibility = View.VISIBLE
            }

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

}