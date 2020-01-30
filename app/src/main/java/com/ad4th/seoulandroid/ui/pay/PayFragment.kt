package com.ad4th.seoulandroid.ui.pay

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.ad4th.seoulandroid.ui.dialog.ConfirmDialog
import com.ad4th.seoulandroid.ui.navigation.NavigationActivity
import com.ad4th.seoulandroid.utils.Constants
import com.ad4th.seoulandroid.utils.PreferenceManager
import com.ad4th.seoulandroid.utils.Utils
import com.bumptech.glide.Glide
import com.squareup.otto.Subscribe
import foundation.icon.icx.data.Bytes
import foundation.icon.icx.data.IconAmount
import kotlinx.android.synthetic.main.fragment_pay.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class PayFragment : Fragment() {

    var view_state = 1
    lateinit var userPojo: UserPojo
    lateinit var introPojo: IntroPojo

    var current_product = 0

    var mDialog : ConfirmDialog? = null


    companion object {
        fun newInstance() = PayFragment()
    }

    private lateinit var viewModel: PayViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        BusProvider.instance.register(this)

        return inflater.inflate(R.layout.fragment_pay, container, false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        BusProvider.instance.unregister(this)
    }



    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if( isVisibleToUser ) {
            drawAll()
            Log.e("[PayFragment]", "isVisibleToUser TRUE")
        }
        else {
            Log.e("[PayFragment]", "isVisibleToUser FALSE ")
//            drawAll()
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PayViewModel::class.java)
        // TODO: Use the ViewModel
        drawAll()
    }

    fun drawAll() {
        // 초기 화면
        if( context == null ) return

        if (!PreferenceManager.isCitizenCheck(context)) {
            changeView(2)
            issue_btn.setOnClickListener {
                BusProvider.instance.post(Events(Constants.CITIZEN_FRAGMENT_MOVE, ""));
            }

            return
        }

        // draw
        changeView(view_state)

        //
        if( view_state == 1 ) {
            val act = activity as NavigationActivity
            userPojo = act.userPojo
            introPojo = act.introPojo

            Log.e("[PayFragment]", "userPojo "  + userPojo.coins[0].balance /*+ " "  + userPojo!!.coins[1].balance + " "  + userPojo!!.coins[2].balance*/ )

            if (userPojo != null && introPojo != null) {

                name_tv.text = userPojo.user.name

                // 자산관리
                total_coin_tv.text = Utils.toNumFormat((userPojo.coins[0].balance /*+ userPojo!!.coins[1].balance + userPojo!!.coins[2].balance*/) / IconAmount.of("1", 18).toLoop())
                // 코인1
                coin1_name_tv.text = userPojo.coins[0].tokenName
                coin1_balance_tv.text = Utils.toNumFormat(userPojo.coins[0].balance / IconAmount.of("1", 18).toLoop())
                coin1_symbol_tv.text = userPojo.coins[0].tokenSymbol
/*
                // 코인2
                coin2_name_tv.text = userPojo!!.coins[1].tokenName
                coin2_balance_tv.text = Utils.toNumFormat(userPojo!!.coins[1].balance / IconAmount.of("1", 18).toLoop())
                coin2_symbol_tv.text = userPojo!!.coins[1].tokenSymbol
                // 코인3
                coin3_name_tv.text = userPojo!!.coins[2].tokenName
                coin3_balance_tv.text = Utils.toNumFormat(userPojo!!.coins[2].balance / IconAmount.of("1", 18).toLoop())
                coin3_symbol_tv.text = userPojo!!.coins[2].tokenSymbol
*/


                qrscan_btn.setOnClickListener {
                    //view_state = 1
                    //changeView(1)
                    val intent = Intent(activity, PayQrActivity::class.java)
                    startActivityForResult(intent, 0x01)  // activity에서 호출을 해야만 결과를 받을수 있다.

                }
            }

        }
        else if( view_state == 2 ) {


            if( current_product == 1 ) {
                product_iv.setImageResource(R.drawable.product1) // 가방으로 고정
                product_name_tv.text = Constants.product1_title
                product_price_tv.text = Utils.toNumFormat(Constants.product1_price / IconAmount.of("1", 18).toLoop())
                product_symbol_tv.text = userPojo!!.coins[0].tokenSymbol
                notice_tv.text = "*"+ userPojo!!.coins[0].tokenName + "으로만 결제가 가능합니다"
            }
            /*
            else if( current_product == 2 ) {
                product_iv.setImageResource(R.drawable.product1)
                product_name_tv.text = Constants.product2_title
                product_price_tv.text = Utils.toNumFormat(Constants.product2_price / IconAmount.of("1", 18).toLoop() )
                product_symbol_tv.text = userPojo!!.coins[1].tokenSymbol
                notice_tv.text = "*"+ userPojo!!.coins[1].tokenName + "으로만 결제가 가능합니다"
            }
            else if( current_product == 3 ) {
                product_iv.setImageResource(R.drawable.product3)
                product_name_tv.text = Constants.product3_title
                product_price_tv.text = Utils.toNumFormat(Constants.product3_price / IconAmount.of("1", 18).toLoop() )
                product_symbol_tv.text = userPojo!!.coins[2].tokenSymbol
                notice_tv.text = "*"+ userPojo!!.coins[2].tokenName + "으로만 결제가 가능합니다"
            }*/


            pay_btn.setOnClickListener{
                view_state = 3
                changeView(view_state)
                redrawFragment()
            }

        }
        else if( view_state == 3 ) {
            Glide.with(this).load(userPojo!!.user.imageUrl).into(auth_picture_iv)
            auth_name_tv.text = userPojo!!.user.name

            if( current_product == 1 ) {
                auth_product_iv.setImageResource(R.drawable.product1)
                auth_product_name_tv.text = Constants.product1_title
                auth_price_tv.text = Utils.toNumFormat(Constants.product1_price / IconAmount.of("1", 18).toLoop())
                auth_symbol_tv.text = userPojo!!.coins[0].tokenSymbol
            }
            /*
            else if( current_product == 2 ) {
                auth_product_iv.setImageResource(R.drawable.product1)
                auth_product_name_tv.text = Constants.product2_title
                auth_price_tv.text = Utils.toNumFormat(Constants.product2_price / IconAmount.of("1", 18).toLoop() )
                auth_symbol_tv.text = userPojo!!.coins[1].tokenSymbol
            }
            else if( current_product == 3 ) {
                auth_product_iv.setImageResource(R.drawable.product3)
                auth_product_name_tv.text = Constants.product3_title
                auth_price_tv.text = Utils.toNumFormat(Constants.product3_price / IconAmount.of("1", 18).toLoop() )
                auth_symbol_tv.text = userPojo!!.coins[2].tokenSymbol
            }
            */

            auth_btn.setOnClickListener {
            /*
                view_state = 4
                changeView(view_state)
                redrawFragment()
                */



                if( current_product == 1 ) {
                    (activity as NavigationActivity).showProgress()
                    if( Constants.product1_price <= userPojo!!.coins[0].balance ) {
                        var task = TokenTask(userPojo!!.coins[current_product - 1].contractAddress,
                                PreferenceManager.getCitizenPrivateKey(context),
                                Constants.ADDRESS_4,
                                (Constants.product1_price / IconAmount.of("1", 18).toLoop()).toString())
                        task.execute()
                    }
                    else {
                        Toast.makeText(context, "잔액이 충분하지 않습니다", Toast.LENGTH_SHORT).show()
                        (activity as NavigationActivity).dismissProgress()
                    }
                }
                /*
                else if( current_product == 2 ) {
                    (activity as NavigationActivity).showProgress()
                    if( Constants.product2_price <= userPojo!!.coins[1].balance ) {
                        var task = TokenTask(userPojo!!.coins[current_product - 1].contractAddress,
                                PreferenceManager.getCitizenPrivateKey(context),
                                Constants.ADDRESS_4,
                                (Constants.product2_price / IconAmount.of("1", 18).toLoop()).toString())
                        task.execute()
                    }
                    else {
                        Toast.makeText(context, "잔액이 충분하지 않습니다", Toast.LENGTH_SHORT).show()
                        (activity as NavigationActivity).dismissProgress()
                    }
                }
                else if( current_product == 3 ) {
                    (activity as NavigationActivity).showProgress()
                    if( Constants.product3_price <= userPojo!!.coins[2].balance ) {
                        var task = TokenTask(userPojo!!.coins[current_product - 1].contractAddress,
                                PreferenceManager.getCitizenPrivateKey(context),
                                Constants.ADDRESS_4,
                                (Constants.product3_price / IconAmount.of("1", 18).toLoop()).toString())
                        task.execute()
                    }
                    else {
                        Toast.makeText(context, "잔액이 충분하지 않습니다", Toast.LENGTH_SHORT).show()
                        (activity as NavigationActivity).dismissProgress()
                    }
                }
                */

            }
            cancel_btn.setOnClickListener {

                mDialog = ConfirmDialog(activity, "",

                        View.OnClickListener {
                            mDialog!!.dismiss()
                        },
                        View.OnClickListener {
                            mDialog!!.dismiss()
                            view_state = 1
                            changeView(view_state)
                            redrawFragment()
                        })
                mDialog!!.show()
            }

        }
        // 결제완료 영수증
        else if( view_state == 4 ) {

            if( current_product == 1 ) {
//                userPojo!!.coins[0].balance = BigInteger("0")
                receipt_name_tv.text = Constants.product1_title
                receipt_price_tv.text = Utils.toNumFormat(Constants.product1_price / IconAmount.of("1", 18).toLoop()) + " " + Constants.product1_symbol
                receipt_coin_tv.text = Constants.product1_name + "/" + Constants.product1_symbol

                receipt_company_tv.text = Constants.product1_company
                receipt_time_tv.text = SimpleDateFormat("yyyy.MM.dd").format(Date())

                PreferenceManager.addAuthLog(context, SimpleDateFormat("yyyy.MM.dd").format(Date()), "인증", "AUTH_LOG_1"/*"서울특별시"*/)
            }
            /*
            else if( current_product == 2 ) {
//                userPojo!!.coins[1].balance = BigInteger("0")

                receipt_name_tv.text = Constants.product2_title
                receipt_price_tv.text = Utils.toNumFormat(Constants.product2_price / IconAmount.of("1", 18).toLoop()) + " " + Constants.product2_symbol
                receipt_time_tv.text = SimpleDateFormat("yyyy.MM.dd").format(Date())
                receipt_coin_tv.text = Constants.product2_name + "/" + Constants.product2_symbol
                PreferenceManager.addAuthLog(context, SimpleDateFormat("yyyy.MM.dd").format(Date()), "인증", "AUTH_LOG_2"/*"아이콘 트래블"*/  )
            }
            else if( current_product == 3 ) {
 //               userPojo!!.coins[2].balance = BigInteger("0")

                receipt_name_tv.text = Constants.product3_title
                receipt_price_tv.text = Utils.toNumFormat(Constants.product3_price / IconAmount.of("1", 18).toLoop()) + " " + Constants.product3_symbol
                receipt_time_tv.text = SimpleDateFormat("yyyy.MM.dd").format(Date())
                receipt_coin_tv.text = Constants.product3_name + "/" + Constants.product3_symbol
                PreferenceManager.addAuthLog(context, SimpleDateFormat("yyyy.MM.dd").format(Date()), "인증", "AUTH_LOG_3"/*"슈퍼스타 아이콘"*/  )
            }
*/
            confirm_btn.setOnClickListener {
                view_state = 1
                changeView(view_state)
                redrawFragment()
            }

//            BusProvider.getInstance().post(Events(Constants.GET_NETWORK_USER, ""));

        }


    }


    fun changeView(view_num : Int) // 0:발행, 1:저장, 2:시민권
    {
        if( view_num == 0 ) {
            layout_0.visibility = View.VISIBLE
            layout_1.visibility = View.GONE
            layout_2.visibility = View.GONE
            layout_3.visibility = View.GONE
            layout_4.visibility = View.GONE
        }
        else if( view_num == 1 ) {
            layout_0.visibility = View.GONE
            layout_1.visibility = View.VISIBLE
            layout_2.visibility = View.GONE
            layout_3.visibility = View.GONE
            layout_4.visibility = View.GONE
        }
        else if( view_num == 2 ) {
            layout_0.visibility = View.GONE
            layout_1.visibility = View.GONE
            layout_2.visibility = View.VISIBLE
            layout_3.visibility = View.GONE
            layout_4.visibility = View.GONE
        }
        else if( view_num == 3 ) {
            layout_0.visibility = View.GONE
            layout_1.visibility = View.GONE
            layout_2.visibility = View.GONE
            layout_3.visibility = View.VISIBLE
            layout_4.visibility = View.GONE
        }
        else if( view_num == 4 ) {
            layout_0.visibility = View.GONE
            layout_1.visibility = View.GONE
            layout_2.visibility = View.GONE
            layout_3.visibility = View.GONE
            layout_4.visibility = View.VISIBLE
        }
        //redrawFragment()
    }
/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode == 0x01 )
        {
            view_state = 2
            changeView(view_state)
            // resultCode를 받아서 물건을 셋팅
        }

    }
*/


    fun onBack() {
        if( view_state == 0 )
        {
            val act = activity as NavigationActivity
            //act.setOnKeyBackPressedListener(null)
            act!!.finish()
        }
        if( view_state == 1 )
        {
            val act = activity as NavigationActivity
            //act.setOnKeyBackPressedListener(null)
            act!!.finish()
        }
        else if( view_state == 2 )
        {
            view_state = 1
            changeView(view_state)
        }
        else if( view_state == 3 )
        {
            view_state = 2
            changeView( view_state)
        }
        else if( view_state == 4 )
        {
            view_state = 3
            changeView( view_state )
        }
        redrawFragment()
    }


    private fun redrawFragment() {
        val ftr = fragmentManager!!.beginTransaction()
        ftr.detach(this@PayFragment).attach(this@PayFragment).commitAllowingStateLoss()
    }




    @Subscribe
    fun EventResult(event: Events) {
//        Log.e("test_event", "Hello" + event.helloEventBus + "!!")
        if( event.index == Constants.PAY_FRAGMENT)
        {
            if( event.message.equals(Constants.product2_qr) ) // 상품은 기존에 2번이었던 MDC를 사용
            {
                current_product = 1
                view_state = 2
                changeView(view_state)
                redrawFragment()
            }
            /*
            else if( event.message.equals(Constants.product2_qr) )
            {
                current_product = 2
                view_state = 2
                changeView(view_state)
                redrawFragment()
            }
            else if( event.message.equals(Constants.product3_qr) )
            {
                current_product = 3
                view_state = 2
                changeView(view_state)
                redrawFragment()
            }
            */
            else
            {
                Toast.makeText(context, "상품 정보를 가져오는 중 오류가 발생하였습니다", Toast.LENGTH_SHORT).show()

            }

        }
        else if( event.index == Constants.PAY_FRAGMENT_BACK)
        {
            onBack()
        }

    }

    private inner class TokenTask(tokenAddress:String, privateKey:String, _toAddress:String, _value:String) : AsyncTask<String, String, String>() {
        var token = Token(tokenAddress, privateKey)
        var hash: Bytes? = null
        val toAddress:String = _toAddress
        val value:String = _value

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            //val token = Token(Constants.EVT1, Constants.PRIVATEKEY_4 )
            hash = token.transfer( toAddress , value)
            Thread.sleep(Constants.WAIT_TIME)
            //val height = token.getBlockHeight(hash)


            // NETWORK USER
            var apiInterface = APIClient.client.create(APIInterface::class.java)
            val address = PreferenceManager.getCitizenAddress(context)
            var call = apiInterface.doGetUser(address);
            call.enqueue(object : Callback<UserPojo?> {
                override fun onResponse(call: Call<UserPojo?>, response: Response<UserPojo?>) {
                    if (response.code() != 200 || response.body() == null) {
                        (activity as NavigationActivity).dismissProgress()
                        Toast.makeText(context, "데이터를 가져오지 못하였습니다", Toast.LENGTH_SHORT).show()
                        return
                    }
                    userPojo = response.body()!!
                    (activity as NavigationActivity).userPojo = userPojo

                    Log.e("[NavigationActivity]", "userPojo " + userPojo!!.coins[0].balance /*+ " " + userPojo!!.coins[1].balance + " " + userPojo!!.coins[2].balance*/)

                    (activity as NavigationActivity).dismissProgress()


                    view_state = 4
                    changeView(view_state)
                    redrawFragment()


                }

                override fun onFailure(call: Call<UserPojo?>, t: Throwable) {
                    call.cancel()
                    (activity as NavigationActivity).dismissProgress()
                    Toast.makeText(context, "데이터를 가져오지 못하였습니다", Toast.LENGTH_SHORT).show()
                }
            })

//            (activity as NavigationActivity).dismissProgress()

            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)


        }
    }


    fun getNetworkUser() {
//        (activity as NavigationActivity).showProgress()
        // address로 사용자 정보 가져오기

    }

}
