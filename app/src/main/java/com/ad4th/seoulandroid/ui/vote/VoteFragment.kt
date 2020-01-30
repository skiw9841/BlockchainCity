package com.ad4th.seoulandroid.ui.vote

import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ad4th.seoulandroid.*
import com.ad4th.seoulandroid.BusEvent.BusProvider
import com.ad4th.seoulandroid.BusEvent.Events
import com.ad4th.seoulandroid.ICON.Token
import com.ad4th.seoulandroid.api.APIClient
import com.ad4th.seoulandroid.api.APIInterface
import com.ad4th.seoulandroid.api.intro.IntroPojo
import com.ad4th.seoulandroid.api.user.UserPojo
import com.ad4th.seoulandroid.ui.dialog.TwoButtonDialog
import com.ad4th.seoulandroid.ui.dialog.ConfirmDialog
import com.ad4th.seoulandroid.ui.navigation.NavigationActivity
import com.ad4th.seoulandroid.utils.Constants
import com.ad4th.seoulandroid.utils.PreferenceManager
import com.ad4th.seoulandroid.utils.Utils
import com.bumptech.glide.Glide
import com.squareup.otto.Subscribe
import foundation.icon.icx.data.Bytes
import kotlinx.android.synthetic.main.fragment_vote.*
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.*


class VoteFragment : Fragment() {

    var view_state = 1
    internal lateinit var mDialog: TwoButtonDialog
    internal lateinit var mCancelDialog: ConfirmDialog

    var candidate_pos = 0
    internal lateinit var userPojo: UserPojo
    internal lateinit var introPojo: IntroPojo
    var vote_sequence = 0

    internal lateinit var block_height: BigInteger
    internal lateinit var txHash: Bytes

    companion object {
        fun newInstance() = VoteFragment()
    }

    private lateinit var viewModel: VoteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        BusProvider.instance.register(this)

        return inflater.inflate(R.layout.fragment_vote, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        BusProvider.instance.unregister(this)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if( isVisibleToUser ) {
            drawAll()
            Log.e("[VoteFragment]", "isVisibleToUser TRUE")
        }
        else {
            Log.e("[VoteFragment]", "isVisibleToUser FALSE ")
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VoteViewModel::class.java)
        drawAll()
    }

    fun drawAll()
    {
        if( context == null ) return

        // 초기 화면
        if (!PreferenceManager.isCitizenCheck(context)) {
            changeView(2)
            issue_btn.setOnClickListener {
                BusProvider.instance.post(Events(Constants.CITIZEN_FRAGMENT_MOVE, ""));
            }
            return
        }


        // draw
        changeView(view_state)

        if( view_state == 1 ) {
            val act = activity as NavigationActivity
            userPojo = act.userPojo
            introPojo = act.introPojo

            // view num 1 : 투표 선택
            if (userPojo != null && introPojo != null) {


                for (i in 0..introPojo.votings!!.size - 1) {
                    if (introPojo.votings?.get(i)!!.sequence == 1) {
                        if( !userPojo!!.user.completedVoting1 ) {

                            // 개행으로 인한 하드코딩
//                            vote_1_title.text = introPojo!!.votings[i].title
                            vote_1_title.text = resources.getString(R.string.vote_description_1)
                            var img: Drawable? = null
                            val into = Glide.with(this).load(introPojo!!.votings?.get(i)!!.imageUrl).into(vote_1_iv)

                            vote_1.setOnClickListener {
                                view_state = 2
                                changeView(view_state)
                                vote_sequence = 1
                                redrawFragment()
                            }
                        }
                        else
                        {
                            vote_1_title.text = ""
                            vote_1_description.text = ""
                            vote_1_iv.setImageResource(R.drawable.img_vote_01_complete)
                            vote_1.setOnClickListener {  }

                        }
                    } else if (introPojo!!.votings?.get(i)!!.sequence == 2) {
                        if( !userPojo!!.user.completedVoting2 ) {
                            // 개행으로 인한 하드코딩
//                            this.vote_2_title.text = introPojo!!.votings[i].title
                            vote_2_title.text = resources.getString(R.string.vote_description_2)
                            var img: Drawable? = null
                            Glide.with(this).load(introPojo!!.votings?.get(i)!!.imageUrl).into(vote_2_iv)

                            vote_2.setOnClickListener {
                                view_state = 2
                                changeView(view_state)
                                vote_sequence = 2
                                redrawFragment()
                            }
                        }
                        else
                        {
                            vote_2_title.text = ""
                            vote_2_description.text = ""
                            vote_2_iv.setImageResource(R.drawable.img_vote_02_complete)
                            vote_2.setOnClickListener {  }
                        }
                    } else if (introPojo!!.votings?.get(i)!!.sequence == 3) {
                        if( !userPojo!!.user.completedVoting3 ) {
                            // 개행으로 인한 하드코딩
//                            this.vote_3_title.text = introPojo!!.votings[i].title
                            vote_3_title.text = resources.getString(R.string.vote_description_3)
                            var img: Drawable? = null
                            Glide.with(this).load(introPojo!!.votings?.get(i)!!.imageUrl).into(vote_3_iv)

                            vote_3.setOnClickListener {
                                view_state = 2
                                changeView(view_state)
                                vote_sequence = 3
                                redrawFragment()
                            }
                        }
                        else
                        {
                            vote_3_title.text = ""
                            vote_3_description.text = ""
                            vote_3_iv.setImageResource(R.drawable.img_vote_03_complete)
                            vote_3.setOnClickListener {  }
                        }

                    }
                }





            }
        }
        else if( view_state == 2 ) {

            // view num 2
            candidate_pos = 0

            if( vote_sequence == 1 ) {
                sub_vote_1_title!!.text = resources.getString(R.string.vote_description_1)
                Glide.with(this).load(introPojo.votings?.get(0)!!.candidates[0].imageUrl).into(candidate_1_iv)
                Glide.with(this).load(introPojo.votings?.get(0)!!.candidates[1].imageUrl).into(candidate_2_iv)
                Glide.with(this).load(introPojo.votings?.get(0)!!.candidates[2].imageUrl).into(candidate_3_iv)
                Glide.with(this).load(introPojo.votings?.get(0)!!.candidates[3].imageUrl).into(candidate_4_iv)
                candidate_1_cb_tv.text = introPojo.votings?.get(0)!!.candidates[0].name
                candidate_2_cb_tv.text = introPojo.votings?.get(0)!!.candidates[1].name
                candidate_3_cb_tv.text = introPojo.votings?.get(0)!!.candidates[2].name
                candidate_4_cb_tv.text = introPojo.votings?.get(0)!!.candidates[3].name
            }
            else if( vote_sequence == 2 ) {
                sub_vote_1_title!!.text = resources.getString(R.string.vote_description_2)
                Glide.with(this).load(introPojo.votings?.get(1)!!.candidates[0].imageUrl).into(candidate_1_iv)
                Glide.with(this).load(introPojo.votings?.get(1)!!.candidates[1].imageUrl).into(candidate_2_iv)
                Glide.with(this).load(introPojo.votings?.get(1)!!.candidates[2].imageUrl).into(candidate_3_iv)
                Glide.with(this).load(introPojo.votings?.get(1)!!.candidates[3].imageUrl).into(candidate_4_iv)
                candidate_1_cb_tv.text = introPojo.votings?.get(1)!!.candidates[0].name
                candidate_2_cb_tv.text = introPojo.votings?.get(1)!!.candidates[1].name
                candidate_3_cb_tv.text = introPojo.votings?.get(1)!!.candidates[2].name
                candidate_4_cb_tv.text = introPojo.votings?.get(1)!!.candidates[3].name
            }
            else if( vote_sequence == 3 ) {
                sub_vote_1_title!!.text = resources.getString(R.string.vote_description_3)
                Glide.with(this).load(introPojo.votings?.get(2)!!.candidates[0].imageUrl).into(candidate_1_iv)
                Glide.with(this).load(introPojo.votings?.get(2)!!.candidates[1].imageUrl).into(candidate_2_iv)
                Glide.with(this).load(introPojo.votings?.get(2)!!.candidates[2].imageUrl).into(candidate_3_iv)
                Glide.with(this).load(introPojo.votings?.get(2)!!.candidates[3].imageUrl).into(candidate_4_iv)
                candidate_1_cb_tv.text = introPojo.votings?.get(2)!!.candidates[0].name
                candidate_2_cb_tv.text = introPojo.votings?.get(2)!!.candidates[1].name
                candidate_3_cb_tv.text = introPojo.votings?.get(2)!!.candidates[2].name
                candidate_4_cb_tv.text = introPojo.votings?.get(2)!!.candidates[3].name
            }

            do_vote.setOnClickListener {
                //if (candidate_pos == 0) return@setOnClickListener
                if( candidate_pos != 0 ) {
                    view_state = 3
                    changeView(view_state)
                    redrawFragment()
                }
            }

            nor_1_view.visibility = View.GONE
            nor_2_view.visibility = View.GONE
            nor_3_view.visibility = View.GONE
            nor_4_view.visibility = View.GONE

            candidate_1_cb_iv.setImageResource(R.drawable.ic_vote_check_nor)
            candidate_2_cb_iv.setImageResource(R.drawable.ic_vote_check_nor)
            candidate_3_cb_iv.setImageResource(R.drawable.ic_vote_check_nor)
            candidate_4_cb_iv.setImageResource(R.drawable.ic_vote_check_nor)


            candidate_1_layout.setOnClickListener {
                candidate_pos = 1
                changeCandidate(1)
                this.do_vote.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            }
            candidate_2_layout.setOnClickListener {
                candidate_pos = 2
                changeCandidate(2)
                this.do_vote.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            }
            candidate_3_layout.setOnClickListener {
                candidate_pos = 3
                changeCandidate(3)
                this.do_vote.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            }
            candidate_4_layout.setOnClickListener {
                candidate_pos = 4
                changeCandidate(4)
                this.do_vote.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            }
        }
        else if( view_state == 3 ) {
            // view num 3
            Glide.with(this).load(userPojo!!.user.imageUrl).into(auth_picture_iv)
            auth_name_tv.text = userPojo!!.user.name
            auth_btn.setOnClickListener {
                (activity as NavigationActivity).showProgress()

                var task = TokenTask(userPojo.votingCoins[vote_sequence-1].contractAddress,
                        PreferenceManager.getCitizenPrivateKey(context),
                        introPojo.votings.get(vote_sequence-1).candidates[candidate_pos-1].walletAddress!!,
                        "1")
                task.execute()

            }

            cancel_btn.setOnClickListener {
                mCancelDialog = ConfirmDialog(activity, "",

                        View.OnClickListener {
                            mCancelDialog!!.dismiss()
                        },
                        View.OnClickListener {
                            mCancelDialog!!.dismiss()
                            view_state = 1
                            changeView(view_state)
                            redrawFragment()
                        })
                mCancelDialog!!.show()

            }



        }

        else if( view_state == 4 ) {
            // view num 4
            name_contents_tv.text = userPojo!!.user.name + "님의 소중한 한표가"
            block_height_tv.text = Utils.toNumFormat(block_height)
            hash_address_tv.text = txHash!!.toHexString(true)

            time_tv.text = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date())

            var name = userPojo!!.user.name
            val len = name.length
            name = name.substring(0,1)
            for( i in 0 .. len-2)
                name+="*"
            hidden_name_tv.text = name
            my_address_tv.text = PreferenceManager.getCitizenAddress(context)
            if( vote_sequence == 1 )
                manager_tv.text = resources.getString(R.string.AUTH_LOG_1)
            else if( vote_sequence == 2 )
                manager_tv.text = resources.getString(R.string.AUTH_LOG_2)
            else if( vote_sequence == 3 )
                manager_tv.text = resources.getString(R.string.AUTH_LOG_3)
            else
                manager_tv.text = ""

            manager_addr_tv.text = userPojo!!.votingCoins[vote_sequence-1].contractAddress
            if( vote_sequence == 1 ) detail_1_tv.text = "공약투표 1건"
            else detail_1_tv.text = "여론조사 1건"
            detail_2_tv.text = introPojo.votings?.get(vote_sequence-1).title

            // 강제로 넣어줌.
            if( vote_sequence == 1 ) userPojo!!.user.completedVoting1 = true
            else if( vote_sequence == 2 ) userPojo!!.user.completedVoting2 = true
            else if( vote_sequence == 3 ) userPojo!!.user.completedVoting3 = true

            // 서버에서 다시 intro 정보를 가져오기
        //    BusProvider.getInstance().post(Events(Constants.GET_NETWORK_USER, ""))
        //    BusProvider.getInstance().post(Events(Constants.GET_NETWORK_INTRO, ""));

            this.vote_back.setOnClickListener {

                mDialog = TwoButtonDialog(activity, "",
                        View.OnClickListener {
                            mDialog!!.dismiss()
                            view_state = 1
                            changeView(view_state)
                            redrawFragment()
                        }
                        ,
                        View.OnClickListener {
                            mDialog!!.dismiss()
                            view_state = 1
                            changeView(view_state)
                            redrawFragment()

                            BusProvider.instance.post(Events(Constants.PAY_FRAGMENT_MOVE, ""))
                            // intro refresh

                            /*
                            view_state = 1
                            changeView(view_state)
                            redrawFragment()
                            */


                        }
                )

                mDialog!!.show()
            }



        }

    }

    override fun onResume() {
        super.onResume()
    }

    fun redrawFragment() {

        val ftr = fragmentManager!!.beginTransaction()
        ftr.detach(this@VoteFragment).attach(this@VoteFragment).commit()

    }




    fun changeView(view_num : Int) // 0:발행, 1:저장, 2:시민권
    {
        if( view_num == 0 ) {
            this.layout_0.visibility = View.VISIBLE
            this.layout_1.visibility = View.GONE
            this.layout_2.visibility = View.GONE
            this.layout_3.visibility = View.GONE
            this.layout_4.visibility = View.GONE
        }
        else if( view_num == 1 ) {
            this.layout_0.visibility = View.GONE
            this.layout_1.visibility = View.VISIBLE
            this.layout_2.visibility = View.GONE
            this.layout_3.visibility = View.GONE
            this.layout_4.visibility = View.GONE
        }
        else if( view_num == 2 ) {
            this.layout_0.visibility = View.GONE
            this.layout_1.visibility = View.GONE
            this.layout_2.visibility = View.VISIBLE
            this.layout_3.visibility = View.GONE
            this.layout_4.visibility = View.GONE
        }
        else if( view_num == 3 ) {
            this.layout_0.visibility = View.GONE
            this.layout_1.visibility = View.GONE
            this.layout_2.visibility = View.GONE
            this.layout_3.visibility = View.VISIBLE
            this.layout_4.visibility = View.GONE
        }
        else if( view_num == 4 ) {
            this.layout_0.visibility = View.GONE
            this.layout_1.visibility = View.GONE
            this.layout_2.visibility = View.GONE
            this.layout_3.visibility = View.GONE
            this.layout_4.visibility = View.VISIBLE
        }

    }

    fun changeCandidate(pos: Int)
    {
        candidate_1_cb_iv.setImageResource(R.drawable.ic_vote_check_nor)
        candidate_2_cb_iv.setImageResource(R.drawable.ic_vote_check_nor)
        candidate_3_cb_iv.setImageResource(R.drawable.ic_vote_check_nor)
        candidate_4_cb_iv.setImageResource(R.drawable.ic_vote_check_nor)

        nor_1_view.visibility = View.VISIBLE
        nor_2_view.visibility = View.VISIBLE
        nor_3_view.visibility = View.VISIBLE
        nor_4_view.visibility = View.VISIBLE

        if( pos == 1 ){
            candidate_1_cb_iv.setImageResource(R.drawable.ic_vote_check_sel)
            nor_1_view.visibility = View.GONE
        }
        else if( pos == 2 ) {
            candidate_2_cb_iv.setImageResource(R.drawable.ic_vote_check_sel)
            nor_2_view.visibility = View.GONE
        }
        else if( pos == 3 ) {
            candidate_3_cb_iv.setImageResource(R.drawable.ic_vote_check_sel)
            nor_3_view.visibility = View.GONE
        }
        else if( pos == 4 ) {
            candidate_4_cb_iv.setImageResource(R.drawable.ic_vote_check_sel)
            nor_4_view.visibility = View.GONE
        }
    }


    fun onBack() {
        if( view_state == 0 ){
            val act = activity as NavigationActivity
            //        act.setOnKeyBackPressedListener(null)
            act!!.finish()
        }
        else if( view_state == 1 )
        {
            val act = activity as NavigationActivity
            //act.setOnKeyBackPressedListener(null)
            act!!.finish()
        }
        else if( view_state == 2 )
        {
            view_state = 1
            changeView( view_state)
        }
        else if( view_state == 3 )
        {
            view_state = 2
            changeView( view_state )
        }
        else if( view_state == 4 )
        {
            view_state = 3
            changeView( view_state )
        }

        redrawFragment()

    }

    @Subscribe
    fun EventResult(event: Events) {
//        Log.e("test_event", "Hello" + event.helloEventBus + "!!")
        if( event.index == Constants.VOTE_FRAGMENT)
        {

        }
        else if( event.index == Constants.VOTE_FRAGMENT_BACK)
        {
            onBack()
        }

    }


    private inner class TokenTask(tokenAddress:String, privateKey:String, _toAddress:String, _value:String) : AsyncTask<String, String, String>() {
        var token = Token(tokenAddress, privateKey)
        var hash: Bytes? = null
        val toAddress:String = _toAddress
        val value:String = _value
        var height: BigInteger? = null

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            //val token = Token(Constants.EVT1, Constants.PRIVATEKEY_4 )
            hash = token.transfer( toAddress , value)
            Thread.sleep(Constants.WAIT_TIME)
            height = token.getBlockHeight(hash)

            var json = JSONObject()
            json.put("votingId", introPojo.votings[vote_sequence-1].id )
            json.put("candidateId", introPojo.votings[vote_sequence - 1].candidates[candidate_pos - 1].id)
            json.put("userId", userPojo!!.user.id )
            json.put("txId", hash!!.toHexString(true))

            val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json.toString());

            var apiInterface = APIClient.client.create(APIInterface::class.java)
            var call = apiInterface.doPostVote( body )

            call.enqueue(object : Callback<Void?> {
                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {


                    Log.d("TAG", response.code().toString())

                    //                            introPojo = response.body()
                    if ( response.code() == 200 )
                    {
                        Thread.sleep(Constants.WAIT_TIME)

                        // NETWORK USER
                        var apiInterface = APIClient.client.create(APIInterface::class.java)
                        val address = PreferenceManager.getCitizenAddress(context)
                        var call = apiInterface.doGetUser(address);
                        call.enqueue(object : Callback<UserPojo?> {
                            override fun onResponse(call: Call<UserPojo?>, response: Response<UserPojo?>) {
                                if( response.code() != 200 || response.body() == null ) {
                                    (activity as NavigationActivity).dismissProgress()
                                    Toast.makeText(context, "데이터를 가져오지 못하였습니다", Toast.LENGTH_SHORT).show()
                                    return
                                }
                                userPojo = response.body()!!
                                (activity as NavigationActivity).userPojo = userPojo
                                Log.e("[VoteFragment]" , "get userPojo "+ userPojo!!.coins[0].balance /*+ " " +  userPojo!!.coins[1].balance + " " +  userPojo!!.coins[2].balance*/  )


                                // NETWORK INTRO
                                var apiInterface = APIClient.client.create(APIInterface::class.java)
                                var call = apiInterface.doGetIntro()

                                call.enqueue(object : Callback<IntroPojo?> {
                                    override fun onResponse(call: Call<IntroPojo?>, response: Response<IntroPojo?>) {
                                        if( response.code() != 200 || response.body() == null ) {
                                            (activity as NavigationActivity).dismissProgress()
                                            Toast.makeText(context, "데이터를 가져오지 못하였습니다", Toast.LENGTH_SHORT).show()
                                            return
                                        }
                                        introPojo = response.body()!!
                                        (activity as NavigationActivity).introPojo = introPojo

                                        // 데이터를 다 가져왔다
                                        block_height = height!!
                                        txHash = hash!!
                                        view_state = 4
                                        changeView(view_state)
                                        redrawFragment()
                                        // 인증
                                        var agency_type = "0" //
                                        if( vote_sequence == 1 )
                                            agency_type = "AUTH_LOG_1" //"아이콘리퍼블릭"
                                        else if( vote_sequence == 2 )
                                            agency_type = "AUTH_LOG_2" //"아이콘 트래블"
                                        else if( vote_sequence == 3 )
                                            agency_type = "AUTH_LOG_3" //"슈퍼스타 아이콘"
                                        else
                                            agency_type = "0"

                                        PreferenceManager.addAuthLog(context, SimpleDateFormat("yyyy.MM.dd").format(Date()), "인증", agency_type)

                                        (activity as NavigationActivity).dismissProgress()
                                    }

                                    override fun onFailure(call: Call<IntroPojo?>, t: Throwable) {
                                        call.cancel()
                                        (activity as NavigationActivity).dismissProgress()
                                        Toast.makeText(context, "데이터를 가져오지 못하였습니다", Toast.LENGTH_SHORT).show()
                                    }
                                })

                            }

                            override fun onFailure(call: Call<UserPojo?>, t: Throwable) {
                                call.cancel()
                                (activity as NavigationActivity).dismissProgress()
                                Toast.makeText(context, "데이터를 가져오지 못하였습니다", Toast.LENGTH_SHORT).show()
                            }
                        })


                    }
                    else
                    {
                        Toast.makeText(context, "response:" + response.code(),Toast.LENGTH_SHORT ).show()
                    }
                    (activity as NavigationActivity).dismissProgress()
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    call.cancel()
                    //                            Toast.makeText(this@NavigationActivity, "데이터를 초기화하지 못하였습니다", Toast.LENGTH_SHORT).show()
                    (activity as NavigationActivity).dismissProgress()
                }
            })



            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)



                /*
                "votingId": 1,
                "candidateId": 1,
                "userId": 2,
                "txId": "aaaaa"
                */



        }
    }
}
