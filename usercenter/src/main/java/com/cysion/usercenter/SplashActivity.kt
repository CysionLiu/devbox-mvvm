package com.cysion.usercenter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cysion.usercenter.ui.activity.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        launchUI {
//            for(x in 1..5){
//                tvCountDown.text=getString(R.string.splash,5-x)
//                delay(1000)
//            }
//            jump()
//        }
        tvCountDown.setOnClickListener {
            jump()
        }
            jump()
    }

    private fun jump() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}
