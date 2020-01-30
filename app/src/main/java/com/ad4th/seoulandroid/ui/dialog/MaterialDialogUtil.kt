package com.ad4th.seoulandroid.ui.dialog

import android.annotation.TargetApi
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import com.ad4th.seoulandroid.R
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback
import com.afollestad.materialdialogs.Theme

object MaterialDialogUtil {
    /**
     * 기본 얼럿 다이얼로그
     *
     * @param context  context
     * @param msgResId Message Resource ID
     */
    fun alert(context: Context, msgResId: Int): MaterialDialog {
        return alert(context, null, context.getString(msgResId), "", "", null, null)
    }

    /**
     * 기본 얼럿 다이얼로그
     *
     * @param context context
     * @param msg     Message String
     */
    fun alert(context: Context, msg: String?): MaterialDialog {
        return alert(context, null, msg, "", "", null, null)
    }

    /**
     * 기본 얼럿 다이얼로그
     *
     * @param context    context
     * @param titleResId Title Resource ID
     * @param msgResId   Message Resource ID
     */
    fun alert(context: Context, titleResId: Int, msgResId: Int): MaterialDialog {
        return alert(context, context.getString(titleResId), context.getString(msgResId), "", "", null, null)
    }

    /**
     * 기본 얼럿 다이얼로그
     */
    fun alert(context: Context, titleResId: Int, msgResId: Int, negativeBtnTxtResId: Int, postiveBtnTxtResId: Int, onNegativeListener: OnNegativeListener?, onPositiveListener: OnPositiveListener?): MaterialDialog {
        return alert(context, if (titleResId == 0) null else context.getString(titleResId),
                context.getString(msgResId), context.getString(negativeBtnTxtResId),
                context.getString(postiveBtnTxtResId), onNegativeListener, onPositiveListener)
    }
    /**
     * 커스텀 다이얼로그 빌드
     */
/*
    public static MaterialDialog customDialog(@NonNull Context context, String titleRes, @NonNull String msgResId, @NonNull String postiveRes, final View.OnClickListener onClickListener) {
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        //테마
        builder.theme(Theme.LIGHT);
        builder.backgroundColorRes(R.color.dialog_bg_white);
        builder.customView(R.layout.layout_custom_alert, false);
        builder.cancelable(false);
        final MaterialDialog dialog = builder.build();
        View view = dialog.getCustomView();

        //타이틀
        if (!TextUtils.isEmpty(titleRes)) ((TextView) view.findViewById(R.id.textViewTitle)).setText(titleRes);
        else view.findViewById(R.id.textViewTitle).setVisibility(View.GONE);
        //컨텐츠
        ((TextView) view.findViewById(R.id.textViewMessage)).setText(msgResId);
        //버튼
        Button button = ((Button) view.findViewById(R.id.buttonConfirm));
        if (!org.apache.http.util.TextUtils.isEmpty(postiveRes)) button.setText(postiveRes);
        else button.setText(R.string.confirm);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) onClickListener.onClick(v);
                dialog.dismiss();
            }
        });

        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            }
        });

        return dialog;
    }
*/
    /**
     * 커스텀 다이얼로그 빌드
     */
/*
    public static MaterialDialog customDialog(@NonNull Context context, int titleResId, int layoutResId, int negativeResId, int postiveResId, final OnNegativeListener onNegativeListener, final OnPositiveListener onPositiveListener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        //테마
        builder.theme(Theme.LIGHT);
        builder.backgroundColorRes(R.color.dialog_bg_white);

        //타이틀
        if (titleResId != 0) builder.title(titleResId);
        //컨텐츠
        if (layoutResId != 0) builder.customView(layoutResId, false);
        //부정 버튼
        if (negativeResId != 0) {
            builder.negativeText(negativeResId);
        } else if (onPositiveListener != null) builder.negativeText(R.string.cancel);
        //긍정버튼
        if (postiveResId != 0) {
            builder.positiveText(postiveResId);
        } else {
            builder.positiveText(R.string.confirm);
        }
        //리스너 셋팅
        builder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                if (onPositiveListener != null) onPositiveListener.onPositive(dialog);
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                super.onNegative(dialog);
                if (onNegativeListener != null) onNegativeListener.onNegative(dialog);
            }
        });
        builder.positiveColorRes(R.color.dialog_color_positive)
                .negativeColorRes(R.color.dialog_color_negative);
        return builder.build();
    }
    */
    /**
     * 기본 얼럿 다이얼로그
     *
     * @param context            context
     * @param title              Title String
     * @param msg                Message String
     * @param onNegativeListener 확인 리스너 (사용하지 않는경우  null)
     * @param onPositiveListener 취소 리스너 (사용하지 않는경우  null)
     */
    /**
     * 기본 얼럿 다이얼로그
     *
     * @param context context
     * @param title   Title String
     * @param msg     Message String
     */
    @JvmOverloads
    fun alert(
            context: Context, title: String?, msg: String?, negativeBtnTxt: String? = "", postiveBtnTxt: String? = "",
            onNegativeListener: OnNegativeListener? = null, onPositiveListener: OnPositiveListener? = null): MaterialDialog {
        val builder = MaterialDialog.Builder(context)
        builder.cancelable(false)
        builder.contentGravity(GravityEnum.CENTER)
        //테마
        builder.theme(Theme.LIGHT)
        //타이틀
        if (!TextUtils.isEmpty(title)) builder.title(title!!)
        //컨텐츠
        if (!TextUtils.isEmpty(msg)) builder.content(msg!!)
        //부정 버튼
        if (!TextUtils.isEmpty(negativeBtnTxt)) {
            builder.negativeText(negativeBtnTxt!!)
        } else if (onNegativeListener != null) builder.negativeText(R.string.cancel)
        //긍정버튼
        if (!TextUtils.isEmpty(postiveBtnTxt)) {
            builder.positiveText(postiveBtnTxt!!)
        } else {
            builder.positiveText(R.string.confirm)
        }
        //리스너 셋팅
        builder.callback(object : ButtonCallback() {
            override fun onPositive(dialog: MaterialDialog) {
                super.onPositive(dialog)
                onPositiveListener?.onPositive(dialog)
            }

            override fun onNegative(dialog: MaterialDialog) {
                super.onNegative(dialog)
                onNegativeListener?.onNegative(dialog)
            }
        })
        builder.positiveColorRes(R.color.colorWhite)
                .negativeColorRes(R.color.colorGray3)
        return builder.build()
    }

    /**
     * 확인버튼 만 있는 얼럿
     */
    fun confirmAlert(context: Context, title: String?, msg: String?, postiveBtnTxt: String?,
                     onPositiveListener: OnPositiveListener?): MaterialDialog {
        val builder = MaterialDialog.Builder(context)
        builder.cancelable(false)
        builder.contentGravity(GravityEnum.CENTER)
        //테마
        builder.theme(Theme.LIGHT)
        //타이틀
        if (!TextUtils.isEmpty(title)) builder.title(title!!)
        //컨텐츠
        if (!TextUtils.isEmpty(msg)) builder.content(msg!!)
        //긍정버튼
        if (!TextUtils.isEmpty(postiveBtnTxt)) {
            builder.positiveText(postiveBtnTxt!!)
        } else {
            builder.positiveText(R.string.confirm)
        }
        //리스너 셋팅
        builder.callback(object : ButtonCallback() {
            override fun onPositive(dialog: MaterialDialog) {
                super.onPositive(dialog)
                onPositiveListener?.onPositive(dialog)
            }

            override fun onNegative(dialog: MaterialDialog) {
                super.onNegative(dialog)
            }
        })
        builder.positiveColorRes(R.color.colorGray4)
                .negativeColorRes(R.color.colorGray3)
        return builder.build()
    }

    /**
     * 기본 얼럿 다이얼로그
     *
     * @param context            context
     * @param title              Title String
     * @param msg                Message String
     * @param itemRes            Single Choice String List
     * @param defaultSelection   초기 선택 아이템의 포지션
     * @param onNegativeListener 확인 리스너 (사용하지 않는경우  null)
     * @param onPositiveListener 취소 리스너 (사용하지 않는경우  null)
     */
    fun singleChoiceAlert(
            context: Context, title: String?, msg: String?, itemRes: Int, defaultSelection: Int,
            negativeBtnTxt: String?, postiveBtnTxt: String?, onNegativeListener: OnNegativeListener?,
            onPositiveListener: OnPositiveListener?, onSelectionListener: OnSelectionListener?): MaterialDialog {
        return singleChoiceAlert(context, title, msg, context.resources.getStringArray(itemRes),
                defaultSelection, negativeBtnTxt, postiveBtnTxt, onNegativeListener, onPositiveListener,
                onSelectionListener)
    }

    fun singleChoiceAlert(
            context: Context, title: String?, msg: String?, data: Array<String?>?, defaultSelection: Int,
            negativeBtnTxt: String?, postiveBtnTxt: String?, onNegativeListener: OnNegativeListener?,
            onPositiveListener: OnPositiveListener?, onSelectionListener: OnSelectionListener?): MaterialDialog {
        val builder = MaterialDialog.Builder(context)
        //테마
        builder.theme(Theme.LIGHT)
        //타이틀
        if (TextUtils.isEmpty(title)) { //builder.title(R.string.alert_title);
        } else {
            builder.title(title!!)
        }
        //컨텐츠
        if (!TextUtils.isEmpty(msg)) builder.content(msg!!)
        if (data != null && data.size > 0) builder.items(*data)
        //부정 버튼
        if (!TextUtils.isEmpty(negativeBtnTxt)) {
            builder.negativeText(negativeBtnTxt!!)
        } else if (onPositiveListener != null) builder.negativeText(R.string.cancel)
        //긍정버튼
        if (!TextUtils.isEmpty(postiveBtnTxt)) {
            builder.positiveText(postiveBtnTxt!!)
        } else {
            builder.positiveText(R.string.confirm)
        }
        //리스너 셋팅
        builder.callback(object : ButtonCallback() {
            override fun onPositive(dialog: MaterialDialog) {
                super.onPositive(dialog)
                onPositiveListener?.onPositive(dialog)
            }

            override fun onNegative(dialog: MaterialDialog) {
                super.onNegative(dialog)
                onNegativeListener?.onNegative(dialog)
            }
        })
        builder.itemsCallbackSingleChoice(defaultSelection
        ) { materialDialog, view, i, charSequence ->
            if (onSelectionListener != null) {
                val size = data!!.size
                if (size > i) {
                    onSelectionListener.onSelection(materialDialog, view, i, charSequence)
                }
            }
            false
        }
        builder.positiveColorRes(R.color.colorGray4)
                .negativeColorRes(R.color.colorGray3)
        return builder.build()
    }

    /**
     * 에러 얼럿
     */
    fun errorAlert(context: Context, msg: String?, onPositiveListener: OnPositiveListener?): MaterialDialog {
        val builder = MaterialDialog.Builder(context)
        //테마
        builder.theme(Theme.LIGHT)
        builder.contentGravity(GravityEnum.CENTER)
        //타이틀
//builder.title(R.string.alert_title);
//컨텐츠
        if (!TextUtils.isEmpty(msg)) builder.content(msg!!)
        //확인버튼
        builder.positiveText(R.string.confirm)
        //리스너 셋팅
        builder.callback(object : ButtonCallback() {
            override fun onPositive(dialog: MaterialDialog) {
                super.onPositive(dialog)
                onPositiveListener?.onPositive(dialog)
            }

            override fun onNegative(dialog: MaterialDialog) {
                super.onNegative(dialog)
            }
        })
        builder.positiveColorRes(R.color.colorGray4)
        return builder.build()
    }

    /**
     * EditText Valid 얼럿 . 얼럿 확인버튼을 누르면 해당에디트 텍스트 포커스로 이동한다.
     */
    fun validEditAlert(
            context: Context, msgResId: Int,
            editText: EditText): MaterialDialog {
        return validEditAlert(context, context.getString(msgResId), editText)
    }

    /**
     * EditText Valid 얼럿 . 얼럿 확인버튼을 누르면 해당에디트 텍스트 포커스로 이동한다.
     */
    fun validEditAlert(
            context: Context, msg: String?,
            editText: EditText): MaterialDialog {
        val builder = MaterialDialog.Builder(context)
        //테마
        builder.theme(Theme.LIGHT)
        //컨텐츠
        if (!TextUtils.isEmpty(msg)) builder.content(msg!!)
        //긍정버튼
        builder.positiveText(R.string.confirm)
        //리스너 셋팅
        builder.callback(object : ButtonCallback() {
            override fun onPositive(dialog: MaterialDialog) {
                super.onPositive(dialog)
                editText.requestFocus(0)
            }

            override fun onNegative(dialog: MaterialDialog) {
                super.onNegative(dialog)
            }
        })
        builder.positiveColorRes(R.color.colorGray4)
                .negativeColorRes(R.color.colorGray3)
        return builder.build()
    }

    /**
     * 마쉬멜로우 퍼미션 체크
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun showPermissionDeniedAlert(context: Activity, deniedTitle: String?, deniedContent: String?) {
        showPermissionDeniedAlert(context, deniedTitle, deniedContent, null)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun showPermissionDeniedAlert(context: Activity, deniedTitle: String?, deniedContent: String?, negativeListener: OnNegativeListener?) {
        if (!context.isFinishing) {
            alert(context, deniedTitle, deniedContent, context.getString(R.string.close), context.getString(R.string.setting), negativeListener, object : OnPositiveListener {
                override fun onPositive(dialog: MaterialDialog) { // OK 를 누르게 되면 설정창으로 이동합니다.
                    try { //Open the specific App Info page:
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.parse("package:" + context.packageName)
                        context.startActivityForResult(intent, 0)
                    } catch (e: ActivityNotFoundException) { //Open the generic Apps page:
                        val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                        context.startActivityForResult(intent, 0)
                    }
                }
            }).show()
        }
    }

    interface OnSelectionListener {
        fun onSelection(dialog: MaterialDialog, view: View?, i: Int, charSequence: CharSequence?)
    }

    /**
     * 긍정 버튼 리스너
     */
    interface OnPositiveListener {
        fun onPositive(dialog: MaterialDialog)
    }

    /**
     * 부정 버튼 리스너
     */
    interface OnNegativeListener {
        fun onNegative(dialog: MaterialDialog)
    }

    /**
     * 부정 버튼 리스너
     */
    interface OnNegativeInputListener {
        fun onNegative(dialog: MaterialDialog, edit: EditText, charSequence: CharSequence?)
    }

    /**
     * 긍정 버튼 리스너
     */
    interface OnPositiveInputListener {
        fun onPositive(dialog: MaterialDialog, edit: EditText, charSequence: CharSequence?)
    }
}