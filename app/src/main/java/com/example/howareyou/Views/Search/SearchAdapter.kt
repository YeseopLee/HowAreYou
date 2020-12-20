package com.example.howareyou.Views.Search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.Views.Detail.DetailActivity
import com.example.howareyou.Model.LoadPostItem
import com.example.howareyou.R
import com.example.howareyou.Util.App
import kotlinx.android.synthetic.main.item_home_posting.view.*
import kotlin.collections.ArrayList

class SearchAdapter(val context: Context, val postingDTO : ArrayList<LoadPostItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType) {
            0 -> {
                var view = LayoutInflater.from(context).inflate(R.layout.item_home_posting,parent,false)
                CustomViewHolder(view)
            }
            else -> throw RuntimeException("에러")
        }

        //return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return postingDTO.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var view = holder.itemView
        var text_content = postingDTO[position].content+"" // Null Exception을 막기위해 공백의 string 추가
        var pre_text_content : String

        view.homeposting_textview_content.text = text_content

        if(text_content.length >= 50){
            pre_text_content = text_content.substring(0,50) + "..."
            view.homeposting_textview_content.text = pre_text_content
        }
        else {
            view.homeposting_textview_content.text = text_content
        }

        //get Code
        when(postingDTO[position].code?.id){
            App.prefs.codeFree -> view.homeposting_textview_boardname.text = "자유게시판"
            App.prefs.codeQA -> view.homeposting_textview_boardname.text = "Q&A"
            App.prefs.codeTips -> view.homeposting_textview_boardname.text = "Tips"
            App.prefs.codeStudy -> view.homeposting_textview_boardname.text = "스터디모집"
            App.prefs.codeCourse -> view.homeposting_textview_boardname.text = "진로게시판"
        }

        view.homeposting_textview_title.text = postingDTO[position].title
        view.homeposting_textview_author.text = postingDTO[position].author
        view.homeposting_textview_date.text = postingDTO[position].createdAt
        view.homeposting_textview_comment.text = postingDTO[position].comments?.size.toString()
        view.homeposting_textview_favorite.text = postingDTO[position].likeds?.size.toString()
        view.homeposting_button_favorite.setBackgroundResource(R.drawable.ic_thumbsup_white)

        // 좋아요 체크
        for ( i in  1..postingDTO[position].likeds!!.size)
        {
            if( postingDTO[position].likeds!![i-1].user_id == App.prefs.myId) view.homeposting_button_favorite.setBackgroundResource(
                R.drawable.ic_thumbsup
            )
        }


        view.setOnClickListener{
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("board_id",postingDTO[position].id)
            context.startActivity(intent)
        }
    }

}