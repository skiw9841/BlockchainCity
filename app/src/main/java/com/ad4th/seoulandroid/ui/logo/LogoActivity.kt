package com.ad4th.seoulandroid.ui.logo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.WindowManager
import com.ad4th.seoulandroid.MainActivity
import com.ad4th.seoulandroid.R

class LogoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        val handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                startActivity(Intent(this@LogoActivity, MainActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.hold)
                finish()   // activity 종료
            }
        }
        handler.sendEmptyMessageDelayed(0, 1500)

    }
}
