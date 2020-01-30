package com.ad4th.seoulandroid.ui.citizen

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.ad4th.seoulandroid.*
import com.ad4th.seoulandroid.BusEvent.BusProvider
import com.ad4th.seoulandroid.api.APIClient
import com.ad4th.seoulandroid.api.APIInterface
import com.ad4th.seoulandroid.api.user.UserPojo
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.fragment_citizen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ad4th.seoulandroid.BusEvent.Events
import com.ad4th.seoulandroid.ui.dialog.MaterialProgressDialog
import com.ad4th.seoulandroid.ui.navigation.NavigationActivity
import com.ad4th.seoulandroid.utils.Constants
import com.ad4th.seoulandroid.utils.PreferenceManager
import com.ad4th.seoulandroid.utils.Utils
import com.bumptech.glide.Glide
import foundation.icon.icx.data.IconAmount
import java.text.SimpleDateFormat
import java.util.*

class CitizenFragment : Fragment() {

    var view_state: Status = Status.BEFORE
    var isCitizen: Boolean = false
    var address: String = ""
    private var userPojo : UserPojo? = null
    var mProgressDialog: MaterialProgressDialog? = null

    companion object {
        fun newInstance() = CitizenFragment()
    }

    private lateinit var viewModel: CitizenViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        BusProvider.instance.register(this)
        return inflater.inflate(R.layout.fragment_citizen, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        BusProvider.instance.unregister(this)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if( isVisibleToUser ) {
            drawCitizen()
            Log.e("[CitizenFragment]", "isVisibleToUser TRUE")
        }
        else {
            Log.e("[CitizenFragment]", "isVisibleToUser FALSE ")
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CitizenViewModel::class.java)
        // TODO: Use the ViewModel

        issue_btn.setOnClickListener {
            //changeView(1)
            val intent = Intent(activity, CitizenQrActivity::class.java)
            activity!!.startActivityForResult(intent, 0x01)  // activity에서 호출을 해야만 결과를 받을수 있다.
        }

        if( !PreferenceManager.isCitizenCheck(activity)) {
            changeView(Status.BEFORE)
        }
        else {
            //address를 가져온다
            address = PreferenceManager.getCitizenAddress(activity)
            getUserData(address)
        }

    }

    override fun onResume() {
        super.onResume()
    }

    fun redrawFragment() {
        val ftr = fragmentManager!!.beginTransaction()
        ftr.detach(this@CitizenFragment).attach(this@CitizenFragment).commit()

    }

    fun changeView(view_num: Status) {
        if( view_num == Status.BEFORE ) {
            layout_1.visibility = View.VISIBLE
            layout_2.visibility = View.GONE
            layout_3.visibility = View.GONE
        }
        else if( view_num == Status.AFTER ) {
            layout_1.visibility = View.GONE
            layout_2.visibility = View.GONE
            layout_3.visibility = View.VISIBLE
        }
    }

    private fun getUserData(address:String) {
        showProgress()
        // address로 사용자 정보 가져오기
        var apiInterface = APIClient.client!!.create(APIInterface::class.java)
        var call = apiInterface.doGetUser(address);
        call!!.enqueue(object : Callback<UserPojo?> {
            override fun onResponse(call: Call<UserPojo?>, response: Response<UserPojo?>) {
                if( response.code() != 200 || response.body() == null ) {
                    Toast.makeText(context, "등록되지 않은 사용자 입니다.", Toast.LENGTH_SHORT).show()
                    dismissProgress()
                    return
                }
                // 인증 (시민증이 저장되어있지 않은데 등록하는 경우에만 발급되었다고 간주)
                if( !PreferenceManager.isCitizenCheck(context))
                    PreferenceManager.addAuthLog(context, SimpleDateFormat("yyyy.MM.dd").format(Date()), "발급", "아이콘리퍼블릭")

                // 시민증 폰에 저장
                PreferenceManager.setCitizenCheck(context, true)
                // 시민증 화면으로 전환
                changeView(Status.AFTER)

                userPojo = response.body()
                val act = activity as NavigationActivity
                act.userPojo = userPojo!!

                // 시민증 데이터 등록
                drawCitizen()

                dismissProgress()
            }

            override fun onFailure(call: Call<UserPojo?>, t: Throwable) {
                call.cancel()
                Toast.makeText(context, "사용자 정보를 가져오는 중 오류가 발생하였습니다", Toast.LENGTH_SHORT).show()
                dismissProgress()
            }
        })
    }

    private fun drawCitizen() {

        if( context == null ) return

        val act = activity as NavigationActivity
        userPojo = act.userPojo

        // view num 1 : 투표 선택
        if (userPojo != null ) {

            // 사진
            Glide.with(this).load(userPojo!!.user.imageUrl).into(picture_iv)
            // 이름
            name_tv.text = userPojo!!.user.name
            // OOO님의 투표권
            name_vote_tv.text = userPojo!!.user.name
            // 투표권 갯수
            var count = 0
            if( !userPojo!!.user.completedVoting1 ) count ++
            if( !userPojo!!.user.completedVoting2 ) count ++
            if( !userPojo!!.user.completedVoting3 ) count ++
            vote_count_tv.text = ""+count
            // 투표권 1
            vote_1_type_tv.text = "공약"
            vote_1_address_tv.text = userPojo!!.votingCoins[0].contractAddress
            if (userPojo!!.user.completedVoting1) { // 투표후
                vote_1_type_tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorGray6))
                vote_1_vote_tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorGray6))
                vote_1_layout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorGrayBg))
                vote_1_stamp_iv.setImageResource(R.drawable.ic_vote_dim)
            } else // 투표전
            {
                vote_1_type_tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
                vote_1_vote_tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
                vote_1_layout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorWhite))
                vote_1_stamp_iv.setImageResource(R.drawable.ic_vote)
                // 클릭시 투표화면으로 이동
                vote_1_layout.setOnClickListener {
                    if (!userPojo!!.user.completedVoting1)
                        BusProvider.instance.post(Events(Constants.VOTE_FRAGMENT_MOVE, ""));
                }
            }
            // 투표권 2
            vote_2_type_tv.text = "여론조사"
            vote_2_address_tv.text = userPojo!!.votingCoins[1].contractAddress
            if (userPojo!!.user.completedVoting2) { // 투표후
                vote_2_type_tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorGray6))
                vote_2_vote_tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorGray6))
                vote_2_layout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorGrayBg))
                vote_2_stamp_iv.setImageResource(R.drawable.ic_vote_dim)
            } else // 투표전
            {
                vote_2_type_tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
                vote_2_vote_tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
                vote_2_layout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorWhite))
                vote_2_stamp_iv.setImageResource(R.drawable.ic_vote)
                // 클릭시 투표화면으로 이동
                vote_2_layout.setOnClickListener {
                    if (!userPojo!!.user.completedVoting2)
                        BusProvider.instance.post(Events(Constants.VOTE_FRAGMENT_MOVE, ""));
                }

            }
            // 투표권 3
            vote_3_type_tv.text = "여론조사"
            vote_3_address_tv.text = userPojo!!.votingCoins[2].contractAddress
            if (userPojo!!.user.completedVoting3) { // 투표후
                vote_3_type_tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorGray6))
                vote_3_vote_tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorGray6))
                vote_3_layout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorGrayBg))
                vote_3_stamp_iv.setImageResource(R.drawable.ic_vote_dim)
            } else // 투표전
            {
                vote_3_type_tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
                vote_3_vote_tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
                vote_3_layout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorWhite))
                vote_3_stamp_iv.setImageResource(R.drawable.ic_vote)
                // 클릭시 투표화면으로 이동
                vote_3_layout.setOnClickListener {
                    if (!userPojo!!.user.completedVoting3)
                        BusProvider.instance.post(Events(Constants.VOTE_FRAGMENT_MOVE, ""));
                }

            }

            // 자산관리
            // modify city app
            krw_tv.text = Utils.toNumFormat((userPojo!!.coins[0].balance /*+ userPojo!!.coins[1].balance + userPojo!!.coins[2].balance*/) / IconAmount.of("1", 18).toLoop())
            // 코인1
            coin1_name_tv.text = userPojo!!.coins[0].tokenName
            coin1_balance_tv.text = Utils.toNumFormat(userPojo!!.coins[0].balance / IconAmount.of("1", 18).toLoop())
            coin1_symbol_tv.text = userPojo!!.coins[0].tokenSymbol

            // 인증관리
            var dateList = PreferenceManager.getAuthLogAll(context, "date")
            var typeList = PreferenceManager.getAuthLogAll(context, "type")
            var agencyList = PreferenceManager.getAuthLogAll(context, "agency")


            auth_list.removeAllViews()
            if( dateList.size == 0 || typeList.size == 0 || agencyList.size == 0 ) return

            for (i in dateList.size - 1 downTo 0) {
                val view = layoutInflater.inflate(R.layout.list_auth, null)
                view.findViewById<TextView>(R.id.date).text = dateList.get(i)
                view.findViewById<TextView>(R.id.type).text = typeList.get(i)
                if( agencyList.get(i).equals("AUTH_LOG_1") )
                    view.findViewById<TextView>(R.id.agency).text = resources.getString(R.string.AUTH_LOG_1)
                else if( agencyList.get(i).equals("AUTH_LOG_2") )
                    view.findViewById<TextView>(R.id.agency).text = resources.getString(R.string.AUTH_LOG_2)
                else  if( agencyList.get(i).equals("AUTH_LOG_3") )
                    view.findViewById<TextView>(R.id.agency).text = resources.getString(R.string.AUTH_LOG_3)
                else
                    view.findViewById<TextView>(R.id.agency).text = agencyList.get(i)

                auth_list.addView(view)
            }
        }

    }
    // MaterialProgressDialog Dismiss 비활성화
    fun showProgress() {
        try {
            mProgressDialog = MaterialProgressDialog()
            mProgressDialog?.show(activity!!)
        } catch (e: Exception) {
            dismissProgress()
        }
    }

    // MaterialProgressDialog Dismiss 비활성화
    fun dismissProgress() {
        if (mProgressDialog != null) mProgressDialog?.dismiss()
    }

    fun onBack() {
        if( view_state == Status.BEFORE ) {
            val act = activity as NavigationActivity
            act!!.finish()
        }
        else if( view_state == Status.AFTER ) {
            val act = activity as NavigationActivity
            act!!.finish()
        }
    }

    @Subscribe
    fun EventResult(event: Events) {
        if( event.index == Constants.CITIZEN_FRAGMENT) {
            var address = event.message
            PreferenceManager.setCitizenAddress(context, address)
            (activity as NavigationActivity).drawDot()
            getUserData(address)
        }
        else if( event.index == Constants.CITIZEN_FRAGMENT_BACK) {
            onBack()
        }
    }
}
