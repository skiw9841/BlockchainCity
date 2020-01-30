package com.ad4th.seoulandroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ad4th.seoulandroid.ui.navigation.NavigationActivity
import com.ad4th.seoulandroid.utils.PreferenceManager
import kotlinx.android.synthetic.main.intro_screen3.view.*
import kotlinx.android.synthetic.main.intro_screen1.view.*
import kotlinx.android.synthetic.main.intro_screen2.view.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if( !PreferenceManager.isIntroCheck(this) )
        {
            PreferenceManager.setIntroCheck(this, false)
            val intent = Intent(this, NavigationActivity::class.java).apply {
                putExtra("","")
            }
            startActivity(intent)
            finish()

        }

        setContentView(R.layout.activity_main)

        val layouts = arrayOf(R.layout.intro_screen1, R.layout.intro_screen2, R.layout.intro_screen3)
        val adapter = ViewPagerAdapters(layouts)
        view_pager.adapter = adapter
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageSelected(position: Int) {
                changeIndicator(position)
            }
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
        })
        changeIndicator(0)




    }


    private val mPagerListener = View.OnClickListener { v ->
        PreferenceManager.setIntroCheck(this, false)
        val intent = Intent(this, NavigationActivity::class.java).apply {
            putExtra("","")
        }
        startActivity(intent)
        finish()
    }

    private fun changeIndicator(position:Int){
        if( position == 0 ) {
            indicator_1_iv.setImageResource(R.drawable.ic_indicator_01)
            indicator_2_iv.setImageResource(R.drawable.ic_indicator_02)
            indicator_3_iv.setImageResource(R.drawable.ic_indicator_02)
            Log.d("Main", "pos 0");
        }
        else if( position == 1 ) {
            indicator_1_iv.setImageResource(R.drawable.ic_indicator_02)
            indicator_2_iv.setImageResource(R.drawable.ic_indicator_01)
            indicator_3_iv.setImageResource(R.drawable.ic_indicator_02)
            Log.d("Main", "pos 1");
        }
        else if( position == 2 ) {
            indicator_1_iv.setImageResource(R.drawable.ic_indicator_02)
            indicator_2_iv.setImageResource(R.drawable.ic_indicator_02)
            indicator_3_iv.setImageResource(R.drawable.ic_indicator_01)
            Log.d("Main", "pos 2");
        }
    }

    private inner class ViewPagerAdapters(private val layouts:Array<Int>): PagerAdapter() {

        override fun instantiateItem(parents: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(parents.context).inflate(layouts.get(position), parents, false)
            if( position == 0 )
            {
                view.next_1_iv.setOnClickListener {
                    view_pager.setCurrentItem(1)
                }
            }
            else if( position == 1 )
            {
                view.next_2_iv.setOnClickListener{
                    view_pager.setCurrentItem(2)
                }
            }
            else if( position == 2 ) {
                view.next_iv.setOnClickListener(mPagerListener)
            }

            parents.addView(view)
            return view
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

}
