package com.example.howareyou.views.detail

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.howareyou.model.*
import com.example.howareyou.R
import com.example.howareyou.App
import com.example.howareyou.databinding.ActivityDetailBinding
import com.example.howareyou.views.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel


@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding>(R.layout.activity_detail) {

    private val detailViewModel by viewModels<DetailViewModel>()
    private lateinit var board_id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        binding.viewModel = detailViewModel

        // home fragment에서 클릭한 게시물의 id를 받아온다
        board_id = intent.getStringExtra("board_id")

//        setButton()
        initAdapter()
        detailViewModel.getValue(board_id)

        //detailViewModel.loadPostingContent(board_id)
        //detailViewModel.getAlarm(App.prefs.myId, board_id)

    }

    //comment recyclerview data / adapter
    var commentDTOList : ArrayList<Comment> = arrayListOf()
    private lateinit var mCommentAdapter : DetailCommentAdapter

    //image recyclerview data / adapter
    var imageList : ArrayList<ImageDTO> = arrayListOf()
    private lateinit var mImageAdapter : DetailImageAdapter

    //comment image uri
    var commentImageUriList : ArrayList<Uri> = arrayListOf()

    //alarm check
    var alarmisRunning : Boolean = false
    var alarm_id : String = ""

    //recomment check
    var recommentisRunning : Boolean = false


//    override fun onResume() {
//        super.onResume()
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        // 대댓글 비활성화
//        recommentisRunning = false
//        App.prefs.tempCommentId = "none"
//        mCommentAdapter.notifyDataSetChanged()
//    }

    private fun initAdapter() {

        mCommentAdapter = DetailCommentAdapter(this)
        binding.detailRecyclerviewComment.adapter = mCommentAdapter

        mImageAdapter = DetailImageAdapter(this)
        binding.detailRecyclerviewImageview.adapter = mImageAdapter
    }

    fun onBack() { // 뒤로가기 버튼
        finish()
    }

//    fun addImage() {
//        TedImagePicker.with(this)
//            .start { uri -> detailViewModel.showSingleImage(uri) }
//    }

//    fun post() {
//        if (App.prefs.tempCommentId == "none") attemptComment(board_id, null)
//        else attemptComment(board_id, App.prefs.tempCommentId)
//        mCommentAdapter.notifyDataSetChanged()
//    }

    private fun attemptComment(board_id: String, comment_id: String?){
        //comment_id에 null이 아닌 값이 들어오면 대댓글
        detail_edittext_comment.error = null
        val content: String = detail_edittext_comment.text.toString()
        var cancel = false
        var focusView: View? = null

        // 유효성 검사
        if (content.isEmpty()){
            detail_edittext_comment.error = "내용을 입력하세요."
            focusView = detail_edittext_comment
            cancel = true
        }

        if(cancel){
            focusView?.requestFocus()
        } else {
            detail_edittext_comment.text = null
            detailViewModel.postComment(
                PostCommentDTO(
                    App.prefs.myEmail,
                    App.prefs.myName,
                    content,
                    App.prefs.myId,
                    board_id,
                    comment_id
                )
            )
            focusView = null
        }
    }

//    override fun onRefresh() {
//        // 데이터 list 초기화
//        commentDTOList.clear()
//        imageList.clear()
//        commentImageUriList.clear()
//        loadPostingContent(board_id)
//        detail_swipelayout.isRefreshing = false
//
//    }
//
//    override fun onBackPressed() {
//        if(App.prefs.tempCommentId == "none") super.onBackPressed()
//        else {
//            App.prefs.tempCommentId = "none"
//        }
//    }
//
//    private fun setButton(){
//
//        detail_swipelayout.setOnRefreshListener(this)
//
//        detail_button_back.setOnClickListener { //뒤로가기 버튼
//            //finish()
//        }
//
//        detail_button_postcomment.setOnClickListener { // 댓글등록
//            if(App.prefs.tempCommentId == "none") attemptComment(board_id, null)
//            else attemptComment(board_id, App.prefs.tempCommentId)
//            mCommentAdapter.notifyDataSetChanged()
//
//        }
//
//        detail_button_addphoto.setOnClickListener { // 댓글 이미지등록
//            TedImagePicker.with(this)
//                .start { uri -> showSingleImage(uri) }
//
//        }
//
//        detail_button_liked.setOnClickListener(object : OnSingleClickListener() { // 좋아요버튼
//            override fun onSingleClick(view: View) {
//                //게시물 or 코멘트 좋아요
//                postLiked(PostLikedDTO(App.prefs.myEmail, App.prefs.myId, board_id, null));
//            }
//        })
//
//        //알람버튼
//        detail_button_notification.setOnClickListener(object : OnSingleClickListener() { //알람버튼
//            override fun onSingleClick(view: View) {
//                if (alarmisRunning) {
//                    deleteAlarm(board_id)
//                    detail_button_notification.setBackgroundResource(R.drawable.ic_notification_gray)
//                    Toast.makeText(applicationContext, "댓글 알림을 받지 않습니다.", Toast.LENGTH_SHORT).show()
//                } else {
//                    postAlarm(board_id)
//                    detail_button_notification.setBackgroundResource(R.drawable.ic_notification)
//                    Toast.makeText(applicationContext, "댓글 알림을 받습니다.", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })
//
//
//        // alert dialog value
//        val builder = AlertDialog.Builder(this).create()
//        detail_button_morevert.setOnClickListener(object : OnSingleClickListener() {
//            override fun onSingleClick(view: View) {
//
//                val dialogView = layoutInflater.inflate(R.layout.activity_more_menu, null)
//                val BtnReport = dialogView.findViewById<Button>(R.id.moremenu_button_report)
//                val BtnRecomment = dialogView.findViewById<Button>(R.id.moremenu_button_recomment)
//                val BtnDelete = dialogView.findViewById<Button>(R.id.moremenu_button_delete)
//                BtnRecomment.visibility = View.GONE
//
//                builder.setView(dialogView)
//                builder.show()
//
//                BtnReport.setOnClickListener {
//                    builder.dismiss()
//                }
//
//                BtnDelete.setOnClickListener {
//                    deletePosting(board_id)
//                    builder.dismiss()
//                }
//
//            }
//        })
//
//    }






}
