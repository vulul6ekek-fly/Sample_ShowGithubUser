package com.fly.sample_showgithubuser.viewblock

import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.fly.sample_showgithubuser.R
import com.fly.sample_showgithubuser.activity.UserDetailActivity
import com.fly.sample_showgithubuser.view.RoundImageView

class UserDetailViewBlock {

    private lateinit var ri_user: RoundImageView
    private lateinit var tv_name: TextView
    private lateinit var tv_bio: TextView
    private lateinit var tv_loginName: TextView
    private lateinit var tv_admin: TextView
    private lateinit var tv_location: TextView
    private lateinit var tv_link: TextView

    constructor(activity: UserDetailActivity) {
        initView(activity)
    }

    fun setBlog(blog: String){
        tv_link.text = blog
    }

    fun setLocation(location: String){
        tv_location.text = location
    }

    fun showAdminBadge(){
        tv_admin.visibility = View.VISIBLE
    }

    fun setLoginName(loginName: String){
        tv_loginName.text = loginName
    }

    fun setBio(bio: String){
        tv_bio.text = bio
    }

    fun setName(name: String){
        tv_name.text = name
    }

    fun setUserPhoto(image: String){
        Glide.with(ri_user.context)
            .load(image)
            .into(ri_user)
    }

    private fun initView(activity: UserDetailActivity){
        ri_user = activity.findViewById(R.id.ri_user)
        tv_name = activity.findViewById(R.id.tv_name)
        tv_bio = activity.findViewById(R.id.tv_bio)
        tv_loginName = activity.findViewById(R.id.tv_loginName)
        tv_admin = activity.findViewById(R.id.tv_admin)
        tv_location = activity.findViewById(R.id.tv_location)
        tv_link = activity.findViewById(R.id.tv_link)
    }
}