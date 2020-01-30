package com.ad4th.seoulandroid.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.BadTokenException
import com.ad4th.seoulandroid.R

class MaterialProgressDialog {
    class CustomDialog(context: Activity?) : Dialog(context, if (mTransparentMode) R.style.TransparentNewDialog else R.style.NewDialog) {
        override fun dismiss() {
            super.dismiss()
            mProgressDialog = null
        }

        companion object {
            fun show(context: Activity?): CustomDialog {
                val dialog = CustomDialog(context)
                val viewGroup = View.inflate(context, R.layout.material_progress_item, null)
                dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP &&
                            !event.isCanceled) {
                        dialog.dismiss()
                        return@OnKeyListener true
                    }
                    false
                })
                dialog.setContentView(viewGroup)
                dialog.setCancelable(false)
                dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                dialog.show()
                return dialog
            }
        }
    }

    /**
     * 프로그래스 다이얼로그 활성화
     */
    fun show(context: Activity) {
        try {
            if (!context.isFinishing) {
                if (mProgressDialog == null) {
                    mTransparentMode = false
                    mProgressDialog = CustomDialog.show(context)
                } else {
                    dismiss()
                    show(context)
                }
            }
        } catch (e: BadTokenException) {
        }
    }

    /**
     * 프로그래스 다이얼로그 활성화
     *
     * @param context
     * @param transparentMode 투명모드 true on false off
     */
    fun show(context: Activity, transparentMode: Boolean) {
        try {
            if (!context.isFinishing) {
                if (mProgressDialog == null) {
                    mTransparentMode = transparentMode
                    mProgressDialog = CustomDialog.show(context)
                } else {
                    dismiss()
                    show(context)
                }
            }
        } catch (e: BadTokenException) {
        }
    }

    /**
     * 프로그래스 다이얼로그 비활성화
     */
    fun dismiss() {
        if (mProgressDialog != null) {
            try {
                if (mProgressDialog!!.isShowing) {
                    mProgressDialog!!.dismiss()
                }
            } catch (error: IllegalArgumentException) {
                mProgressDialog!!.cancel()
            }
        }
    }

    companion object {
        private var mProgressDialog: CustomDialog? = null
        private val TAG = MaterialProgressDialog::class.java.name
        private var mTransparentMode = false// 배경 투명모드 = false
    }
}