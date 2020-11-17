package com.example.howareyou

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.Model.LoadPostDTO
import com.example.howareyou.Model.LoadPostItem
import com.example.howareyou.Model.PostingDTO
import kotlinx.android.synthetic.main.item_posting.view.*
import java.util.*
import kotlin.collections.ArrayList

class PostingAdapter(val context: Context, val postingDTO : ArrayList<LoadPostItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable{

    var postingDTOfilter : ArrayList<LoadPostItem>
    init {
        postingDTOfilter = postingDTO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType) {
            0 -> {
                var view = LayoutInflater.from(context).inflate(R.layout.item_posting,parent,false)
                CustomViewHolder(view)
            }
            else -> throw RuntimeException("에러")
        }

        //return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return postingDTOfilter.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var view = holder.itemView
        var text_content = postingDTOfilter[position].content+"" // Null Exception을 막기위해 공백의 string 추가
        var pre_text_content : String

        view.posting_textview_content.text = text_content

        if(text_content.length >= 100){
            pre_text_content = text_content.substring(0,100) + "..."

            view.posting_textview_content.text = pre_text_content
        }
        else {
            view.posting_textview_content.text = text_content
        }

        view.posting_textview_title.text = postingDTOfilter[position].title
        view.posting_textview_author.text = postingDTOfilter[position].author
        view.posting_textview_date.text = postingDTOfilter[position].createdAt
        view.posting_textview_comment.text = postingDTOfilter[position].comments?.size.toString()
        view.posting_textview_favorite.text = postingDTOfilter[position].likeds?.size.toString()

        view.setOnClickListener{
            val intent = Intent(context,DetailActivity::class.java)
            intent.putExtra("board_id",postingDTOfilter[position].id)
            context.startActivity(intent)
        }

//        view.profile_layout.setOnClickListener {
//            val iT = Intent(context,ProfileActivity::class.java)
//            iT.putExtra("name",friendDTOfilter[position].name)
//            iT.putExtra("username",username)
//            context.startActivity(iT)
//        }

    }

    /* 검색기능 */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    postingDTOfilter = postingDTO
                } else {
                    val resultList : LoadPostDTO? = null
                    for (row in postingDTO)
                        if (row.title!!.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList?.add(row)
                    } else if (row.content!!.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList?.add(row)
                        }
                    postingDTOfilter = resultList!!
                }
                val filterResults = FilterResults()
                filterResults.values = postingDTOfilter
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                postingDTOfilter = results?.values as LoadPostDTO
                notifyDataSetChanged()
            }

        }

    }
}