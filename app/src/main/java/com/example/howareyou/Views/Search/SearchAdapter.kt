package com.example.howareyou.views.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.views.detail.DetailActivity
import com.example.howareyou.model.LoadPostItem
import com.example.howareyou.R
import com.example.howareyou.App
import com.example.howareyou.databinding.ItemHomePostingBinding
import com.example.howareyou.model.Code
import com.example.howareyou.util.ConvertTime
import com.example.howareyou.views.home.CustomViewHolder
import kotlinx.android.synthetic.main.item_home_posting.view.*
import java.lang.Exception
import kotlin.collections.ArrayList

class SearchAdapter(val context: Context) : RecyclerView.Adapter<SearchViewHolder>(){


    val postingDTO = ArrayList<LoadPostItem>()

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

    fun setItem(data: ArrayList<LoadPostItem>){
        this.postingDTO.clear()
        this.postingDTO.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {

        val binding = ItemHomePostingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return postingDTO.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        holder.onBind(postingDTO[position])


        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("board_id",postingDTO[position].id)
            context.startActivity(intent)
        }

        // 시간 convert
        val tempText: String
        val convertTime = ConvertTime()
        try {
            postingDTO[position].createdAt = convertTime.showTime(postingDTO[position].createdAt)

        } catch (e : Exception){

        }

        //get Code
        when(postingDTO[position].code?.id){
            App.prefs.codeFree -> postingDTO[position].code = Code("자유게시판")
            App.prefs.codeQA -> postingDTO[position].code = Code("Q&A")
            App.prefs.codeTips -> postingDTO[position].code = Code("Tips")
            App.prefs.codeStudy -> postingDTO[position].code = Code("스터디모집")
            App.prefs.codeCourse -> postingDTO[position].code = Code("진로게시판")
        }

        holder.itemView.homeposting_button_favorite.setBackgroundResource(R.drawable.ic_thumbsup_white)


//         좋아요 체크
        for ( i in  1..postingDTO[position].likeds!!.size)
        {
            if( postingDTO[position].likeds!![i-1].user_id == App.prefs.myId) holder.itemView.homeposting_button_favorite.setBackgroundResource(
                R.drawable.ic_thumbsup
            )
        }



    }

//        var view = holder.itemView
//        var text_content = postingDTO[position].content+"" // Null Exception을 막기위해 공백의 string 추가
//        var pre_text_content : String
//
//        view.homeposting_textview_content.text = text_content
//
//        if(text_content.length >= 50){
//            pre_text_content = text_content.substring(0,50) + "..."
//            view.homeposting_textview_content.text = pre_text_content
//        }
//        else {
//            view.homeposting_textview_content.text = text_content
//        }
//
//        //get Code
//        when(postingDTO[position].code?.id){
//            App.prefs.codeFree -> view.homeposting_textview_boardname.text = "자유게시판"
//            App.prefs.codeQA -> view.homeposting_textview_boardname.text = "Q&A"
//            App.prefs.codeTips -> view.homeposting_textview_boardname.text = "Tips"
//            App.prefs.codeStudy -> view.homeposting_textview_boardname.text = "스터디모집"
//            App.prefs.codeCourse -> view.homeposting_textview_boardname.text = "진로게시판"
//        }
//
//        view.homeposting_textview_title.text = postingDTO[position].title
//        view.homeposting_textview_author.text = postingDTO[position].author
//        view.homeposting_textview_date.text = postingDTO[position].createdAt
//        view.homeposting_textview_comment.text = postingDTO[position].comments?.size.toString()
//        view.homeposting_textview_favorite.text = postingDTO[position].likeds?.size.toString()
//        view.homeposting_button_favorite.setBackgroundResource(R.drawable.ic_thumbsup_white)
//
//        // 좋아요 체크
//        for ( i in  1..postingDTO[position].likeds!!.size)
//        {
//            if( postingDTO[position].likeds!![i-1].user_id == App.prefs.myId) view.homeposting_button_favorite.setBackgroundResource(
//                R.drawable.ic_thumbsup
//            )
//        }
//
//
//        view.setOnClickListener{
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra("board_id",postingDTO[position].id)
//            context.startActivity(intent)
//        }
//    }

}

class SearchViewHolder(val binding : ItemHomePostingBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data : LoadPostItem?){
        binding.postItem = data
    }
}