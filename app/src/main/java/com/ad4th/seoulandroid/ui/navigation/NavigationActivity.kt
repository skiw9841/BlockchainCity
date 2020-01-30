package com.ad4th.seoulandroid.ui.navigation

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ad4th.seoulandroid.BusEvent.BusProvider
import com.ad4th.seoulandroid.BusEvent.Events
import com.ad4th.seoulandroid.utils.Constants
import com.ad4th.seoulandroid.utils.PreferenceManager
import com.ad4th.seoulandroid.R
import com.ad4th.seoulandroid.api.APIClient
import com.ad4th.seoulandroid.api.APIInterface
import com.ad4th.seoulandroid.api.intro.IntroPojo
import com.ad4th.seoulandroid.api.user.UserPojo
import com.ad4th.seoulandroid.ui.citizen.CitizenFragment
import com.ad4th.seoulandroid.ui.dialog.MaterialProgressDialog
import com.ad4th.seoulandroid.ui.pay.PayFragment
import com.ad4th.seoulandroid.ui.vote.VoteFragment
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.tab1_layout.view.*
import kotlinx.android.synthetic.main.tab2_layout.view.*
import kotlinx.android.synthetic.main.tab3_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NavigationActivity : AppCompatActivity() {

    private var current_fragment : Int = 0
    internal lateinit var tab1: TabLayout.Tab
    internal lateinit var tab2: TabLayout.Tab
    internal lateinit var tab3: TabLayout.Tab

    internal lateinit var userPojo : UserPojo
    internal lateinit var introPojo : IntroPojo
    var citizenFragment : CitizenFragment? = null
    var voteFragment : VoteFragment? = null
    var payFragment : PayFragment? = null

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    var mProgressDialog: MaterialProgressDialog? = null

    var clear_cnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        BusProvider.instance.register(this)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        viewPager.adapter = mSectionsPagerAdapter

        // network get Intro
        showProgress()
        var apiInterface = APIClient.client.create(APIInterface::class.java)
        var call = apiInterface.doGetIntro()

        call!!.enqueue(object : Callback<IntroPojo?> {
            override fun onResponse(call: Call<IntroPojo?>, response: Response<IntroPojo?>) {
                Log.d("TAG", response.code().toString())

                var displayResponse = ""

                introPojo = response.body()!!
                dismissProgress()
            }

            override fun onFailure(call: Call<IntroPojo?>, t: Throwable) {
                call.cancel()
                Toast.makeText(this@NavigationActivity, getString(R.string.fail_msg_cannot_init), Toast.LENGTH_SHORT).show()
                dismissProgress()
            }
        })

        tab1 = tabLayout.getTabAt(0)!!
        tab1.setCustomView(R.layout.tab1_layout)
        tab2 = tabLayout.getTabAt(1)!!
        tab2.setCustomView(R.layout.tab2_layout)
        tab3 = tabLayout.getTabAt(2)!!
        tab3.setCustomView(R.layout.tab3_layout)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val view = tab.customView

                if( tab.position == 0 ) {
                    view!!.nav_1_text.setTextColor(ContextCompat.getColor(this@NavigationActivity, R.color.colorBlack))
                    current_fragment = Constants.CITIZEN_FRAGMENT
                }
                else if( tab.position == 1 ) {
                    view!!.nav_2_text.setTextColor(ContextCompat.getColor(this@NavigationActivity, R.color.colorBlack))
                    view!!.dot_1.visibility = View.GONE
                    current_fragment = Constants.VOTE_FRAGMENT
                }
                else if( tab.position == 2 ) {
                    view!!.nav_3_text.setTextColor(ContextCompat.getColor(this@NavigationActivity, R.color.colorBlack))
                    view!!.dot_2.visibility = View.GONE
                    current_fragment = Constants.PAY_FRAGMENT
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val view = tab.customView
                if( tab.position == 0 )
                    view!!.nav_1_text.setTextColor(ContextCompat.getColor( this@NavigationActivity, R.color.colorGray5))
                else if( tab.position == 1 )
                    view!!.nav_2_text.setTextColor(ContextCompat.getColor( this@NavigationActivity, R.color.colorGray5))
                else if( tab.position == 2 )
                    view!!.nav_3_text.setTextColor(ContextCompat.getColor( this@NavigationActivity, R.color.colorGray5))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })

        logo_iv.setOnClickListener {

            clear_cnt++
            if( clear_cnt > 5 )
            {
                var alertDialogBuilder:AlertDialog.Builder = AlertDialog.Builder(this@NavigationActivity)

                alertDialogBuilder
                        .setMessage(R.string.msg_delete)
                        .setPositiveButton("삭제",
                                DialogInterface.OnClickListener { dialog, id ->
                                    // 프로그램을 종료한다
                                    PreferenceManager.clearAll(this@NavigationActivity)
                                    this@NavigationActivity.finish()
                                    clear_cnt = 0
                                })
                        .setNegativeButton("취소",
                                DialogInterface.OnClickListener { dialog, id ->
                                    // 다이얼로그를 취소한다
                                    dialog.cancel()
                                    clear_cnt = 0
                                })
                        .create().show()


            }
        }

    }

    fun getNetworkIntro(){
        var apiInterface = APIClient.client.create(APIInterface::class.java)
        var call = apiInterface.doGetIntro()

        call!!.enqueue(object : Callback<IntroPojo?> {
            override fun onResponse(call: Call<IntroPojo?>, response: Response<IntroPojo?>) {
                if( response.code() != 200 || response.body() == null ) {
                    dismissProgress()
                    Toast.makeText(this@NavigationActivity, getString(R.string.fail_msg_cannot_get), Toast.LENGTH_SHORT).show()
                    return
                }
                introPojo = response.body()!!
            }

            override fun onFailure(call: Call<IntroPojo?>, t: Throwable) {
                call.cancel()
                Toast.makeText(this@NavigationActivity, getString(R.string.fail_msg_cannot_get), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getNetworkUser() {
        // address로 사용자 정보 가져오기
        var apiInterface = APIClient.client.create(APIInterface::class.java)
        val address = PreferenceManager.getCitizenAddress(applicationContext)
        var call = apiInterface.doGetUser(address);
        call!!.enqueue(object : Callback<UserPojo?> {
            override fun onResponse(call: Call<UserPojo?>, response: Response<UserPojo?>) {
                if( response.code() != 200 || response.body() == null ) {
                    Toast.makeText(this@NavigationActivity, getString(R.string.fail_msg_cannot_get), Toast.LENGTH_SHORT).show()
                    return
                }
                userPojo = response.body()!!
                Log.e("[NavigationActivity]", "userPojo "  + userPojo!!.coins[0].balance /*+ " "  + userPojo!!.coins[1].balance + " "  + userPojo!!.coins[2].balance*/ )
            }

            override fun onFailure(call: Call<UserPojo?>, t: Throwable) {
                call.cancel()
                Toast.makeText(this@NavigationActivity, getString(R.string.fail_msg_cannot_get), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        BusProvider.instance.unregister(this)
        super.onDestroy()
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            // getItem is called to instantiate the fragment for the given page.
            when (position) {
                0 -> {
                    citizenFragment = CitizenFragment.newInstance()
                    return citizenFragment
                }
                1 -> {
                    voteFragment = VoteFragment.newInstance()
                    return voteFragment // VoteFragment.newInstance()
                }
                2 -> {
                    payFragment = PayFragment.newInstance()
                    return payFragment//PayFragment.newInstance()
                }
            }
            return null

        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }


    override fun onBackPressed() {
        if( current_fragment == Constants.CITIZEN_FRAGMENT)
            BusProvider.instance.post(Events(Constants.CITIZEN_FRAGMENT_BACK, ""));
        else if( current_fragment == Constants.VOTE_FRAGMENT)
            BusProvider.instance.post(Events(Constants.VOTE_FRAGMENT_BACK, ""));
        else if( current_fragment == Constants.PAY_FRAGMENT)
            BusProvider.instance.post(Events(Constants.PAY_FRAGMENT_BACK, ""));
        else
            super.onBackPressed()
    }
    // MaterialProgressDialog Dismiss 비활성화
    fun showProgress() {
        try {
            mProgressDialog = MaterialProgressDialog()
            mProgressDialog?.show(this)
        } catch (e: Exception) {
            dismissProgress()
        }
    }

    // MaterialProgressDialog Dismiss 비활성화
    fun dismissProgress() {
        if (mProgressDialog != null) mProgressDialog?.dismiss()
    }

    fun drawDot() {
        tab2!!.customView!!.dot_1.visibility = View.VISIBLE
        tab3!!.customView!!.dot_2.visibility = View.VISIBLE
    }


    @Subscribe
    fun EventResult(event: Events) {
        if( event.index == Constants.CITIZEN_FRAGMENT_MOVE) {
            viewPager.setCurrentItem(0, true)
        }
        else if( event.index == Constants.VOTE_FRAGMENT_MOVE) {
            viewPager.setCurrentItem(1, true)
        }
        else if( event.index == Constants.PAY_FRAGMENT_MOVE) {
            viewPager.setCurrentItem(2, true)
        }
        else if( event.index == Constants.GET_NETWORK_INTRO) {
            Handler().postDelayed(Runnable {

                getNetworkIntro()
            }, 10)

        }
        else if( event.index == Constants.GET_NETWORK_USER) {
            Handler().postDelayed(Runnable {

                getNetworkUser()
            }, 10)

        }

    }

    override fun onResume() {
        super.onResume()
    }

}