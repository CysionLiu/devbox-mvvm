package com.cysion.usercenter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cysion.other.startActivity_ex
import com.cysion.usercenter.ui.activity.MainActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        scope.launch {
            delay(500)
            startActivity_ex<MainActivity>()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
