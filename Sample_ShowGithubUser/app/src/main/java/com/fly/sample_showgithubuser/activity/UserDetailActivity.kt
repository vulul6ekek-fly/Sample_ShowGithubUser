package com.fly.sample_showgithubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fly.sample_showgithubuser.R
import com.fly.sample_showgithubuser.extension.KEY_LIGIN

class UserDetailActivity : AppCompatActivity() {

    private var login: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        getIntentValue()
    }

    private fun getIntentValue() {
        login = intent.getStringExtra(KEY_LIGIN)
    }
}