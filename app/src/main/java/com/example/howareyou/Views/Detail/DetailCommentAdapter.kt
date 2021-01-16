package com.example.howareyou.views.detail

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howareyou.App
import com.example.howareyou.R
import com.example.howareyou.databinding.ItemCommentBinding
import com.example.howareyou.databinding.ItemHomePostingBinding
import com.example.howareyou.databinding.ItemRecommentBinding
import com.example.howareyou.model.Comment
import com.example.howareyou.model.LoadPostItem
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.util.ConvertTime
import com.example.howareyou.util.OnSingleClickListener
import kotlinx.android.synthetic.main.activity_imageview_detail.view.*
import kotlinx.android.synthetic.main.item_comment.view.*
import java.lang.Exception


class DetailCommentAdapter(val context: Context) : RecyclerView.Adapter<CustomViewHolder>() {


    var commentArray = ArrayList<Comment>()

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

    fun setItem(data: ArrayList<Comment>) {
        this.commentArray.clear()
        this.commentArray.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {


        return when (viewType) {
            0 -> {
                val binding = ItemCommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CustomViewHolder(binding)
            }

            1 -> {
                val binding = ItemRecommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CustomViewHolder(binding)
            }

            else -> throw RuntimeException("error")
        }
    }


    override fun getItemCount(): Int {
        return commentArray.size
    }

    override fun getItemViewType(position: Int): Int {

        Log.e("viewtype", commentArray[position].comment.toString())
        return if (commentArray[position].comment != null) {
            1
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        // alert dialog value
        val builder = AlertDialog.Builder(context).create()

        // Get the LayoutInflater from Context
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)

        Log.e("test0111",commentArray.size.toString())
        Log.e("test0222",commentArray.toString())


        // 댓글
        //if (holder.itemViewType == 0){
        holder.onBind(commentArray[position])

//            var view = holder.itemView
//            view.comment_textview_content.text = commentArray[position].content
//            view.comment_textview_author.text = commentArray[position].author
//            view.comment_textview_liked.text = commentArray[position].likeds?.size.toString()
//
//            // 시간 convert
//            val convtime = ConvertTime()
//            view.comment_textview_date.text = convtime.showTime(commentArray[position].createdAt)
        // 시간 convert
        val convertTime = ConvertTime()
        try {
            commentArray[position].createdAt =
                convertTime.showTime(commentArray[position].createdAt)

        } catch (e: Exception) {
        }
        // 좋아요
//            view.comment_imageview_liked.setOnClickListener {
//                postLiked(
//                    PostLikedDTO(
//                        App.prefs.myEmail,
//                        App.prefs.myId,
//                        null,
//                        commentArray[position].id
//                    ), view, holder
//                )
//            }

        // 사용자 좋아요 상태 체크
//            for (i in 0 until (commentArray[position].likeds?.size!!)) {
//                if (commentArray[position].likeds!![i].user_id == App.prefs.myId) {
//                    view.comment_imageview_liked.setBackgroundResource(R.drawable.ic_thumbsup)
//                }
//            }
//
//            //이미지
        if (holder.itemViewType == 0) {
            var tempUrl = commentArray[position].image?.formats?.thumbnail?.url
            var detailUrl = commentArray[position].image?.url
            if (!tempUrl.isNullOrEmpty()) {
                Glide.with(holder.itemView).load(RetrofitClient.BASE_URL + tempUrl)
                    .into(holder.itemView.comment_imageview_image)
                holder.itemView.comment_imageview_image.visibility = View.VISIBLE
            }

            holder.itemView.comment_imageview_image.setOnClickListener {

                val dialogView = layoutInflater.inflate(R.layout.activity_imageview_detail, null)
                Glide.with(dialogView).load(RetrofitClient.BASE_URL + detailUrl)
                    .into(dialogView.imageview_detail)
                builder.setView(dialogView)
                builder.show()

            }
//
            //더보기 버튼
            holder.itemView.comment_button_morevert.setOnClickListener(object :
                OnSingleClickListener() {
                override fun onSingleClick(view_: View) {

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
                        App.prefs.tempCommentId = commentArray[position].id
                        builder.dismiss()
                    }

                }
            })

        }
    }

//        else{ // 대댓글
//            holder.onBind(commentArray[position])
//
////            var view = holder.itemView
////            view.recomment_textview_content.text = commentArray[position].content
////            view.recomment_textview_author.text = commentArray[position].author
////            view.recomment_textview_liked.text = commentArray[position].likeds?.size.toString()
////
////            // 시간 convert
////            val convtime = ConvertTime()
////            view.recomment_textview_date.text = convtime.showTime(commentArray[position].createdAt)
//            val convertTime = ConvertTime()
//            try {
//                commentArray[position].createdAt = convertTime.showTime(commentArray[position].createdAt)
//
//            } catch (e : Exception){}
//            // 좋아요
////            view.recomment_imageview_liked.setOnClickListener {
////                postLiked(
////                    PostLikedDTO(
////                        App.prefs.myEmail,
////                        App.prefs.myId,
////                        null,
////                        commentArray[position].id
////                    ), view, holder
////                )
////            }
//
//            // 사용자 좋아요 상태 체크
//
////            for (i in 0 until (commentArray[position].likeds?.size!!)) {
////                if (commentArray[position].likeds!![i].user_id == App.prefs.myId) {
////                    view.recomment_imageview_liked.setBackgroundResource(R.drawable.ic_thumbsup)
////                }
////            }
////
////            // 이미지
////            var tempUrl = commentArray[position].image?.formats?.thumbnail?.url
////            var detailUrl = commentArray[position].image?.url
////            if(!tempUrl.isNullOrEmpty())
////            {
////                Glide.with(view).load(RetrofitClient.BASE_URL + tempUrl).into(view.recomment_imageview_image)
////                view.recomment_imageview_image.visibility = View.VISIBLE
////            }
////
////            view.recomment_imageview_image.setOnClickListener {
////
////                val dialogView = layoutInflater.inflate(R.layout.activity_imageview_detail, null)
////                Glide.with(dialogView).load(RetrofitClient.BASE_URL + detailUrl).into(dialogView.imageview_detail)
////                builder.setView(dialogView)
////                builder.show()
////
////
////            }
////
////            //더보기 버튼
////            view.recomment_button_morevert.setOnClickListener(object : OnSingleClickListener() {
////                override fun onSingleClick(view: View) {
////
////                    val dialogView = layoutInflater.inflate(R.layout.activity_more_menu, null)
////                    val BtnReport = dialogView.findViewById<Button>(R.id.moremenu_button_report)
////                    val BtnRecomment =
////                        dialogView.findViewById<Button>(R.id.moremenu_button_recomment)
////                    BtnRecomment.visibility = View.GONE
////
////                    builder.setView(dialogView)
////                    builder.show()
////
////                    BtnReport.setOnClickListener {
////                        builder.dismiss()
////                    }
////
////                }
////            })
//        }
//
//    }
//
//
}


class CustomViewHolder : RecyclerView.ViewHolder {
    private lateinit var itemCommentBinding: ItemCommentBinding
    private lateinit var itemRecommentBinding: ItemRecommentBinding
    private var branch: Boolean = true

    constructor(binding: ItemCommentBinding) : super(binding.root) {
        itemCommentBinding = binding
        branch = true
    }

    constructor(binding: ItemRecommentBinding) : super(binding.root) {
        itemRecommentBinding = binding
        branch = false
    }

    fun onBind(data: Comment) {
        when (branch) {
            true -> itemCommentBinding.commentItem = data
            false -> itemRecommentBinding.commentItem = data
        }
    }

}

//class CustomViewHolder(val binding : ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
//    fun onBind(data : Comment?){
//        binding.commentItem = data
//    }
//}