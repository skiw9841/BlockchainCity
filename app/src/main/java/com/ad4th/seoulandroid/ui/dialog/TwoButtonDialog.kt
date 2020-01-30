package com.ad4th.seoulandroid.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.ad4th.seoulandroid.R

class TwoButtonDialog : Dialog {
    private var mContentView: TextView? = null
    private var mLeftButton: Button? = null
    private var mRightButton: Button? = null
    private var mContent: String
    private var mLeftClickListener: View.OnClickListener? = null
    private var mRightClickListener: View.OnClickListener?
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 다이얼로그 외부 화면 흐리게 표현
        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.8f
        window.attributes = lpWindow
        setContentView(R.layout.dialog_twobtn)
        mContentView = findViewById<View>(R.id.txt1_content) as TextView
        mLeftButton = findViewById<View>(R.id.btn_left) as Button
        mRightButton = findViewById<View>(R.id.btn_right) as Button
        // 제목과 내용을 생성자에서 셋팅한다.
//mContentView.setText(Html.fromHtml(mContent));
// 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null) {
            mLeftButton!!.setOnClickListener(mLeftClickListener)
            mRightButton!!.setOnClickListener(mRightClickListener)
        } else if (mLeftClickListener != null
                && mRightClickListener == null) {
            mLeftButton!!.setOnClickListener(mLeftClickListener)
        } else {
        }
    }

    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    constructor(context: Context?, content: String,
                singleListener: View.OnClickListener?) : super(context) {
        mContent = content
        mRightClickListener = singleListener
    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    constructor(context: Context?, content: String,
                leftListener: View.OnClickListener?,
                rightListener: View.OnClickListener?) : super(context) {
        mContent = content
        mLeftClickListener = leftListener
        mRightClickListener = rightListener
    }
}