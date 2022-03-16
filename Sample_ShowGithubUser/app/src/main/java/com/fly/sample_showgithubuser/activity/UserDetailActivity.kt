package com.fly.sample_showgithubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.fly.sample_showgithubuser.R
import com.fly.sample_showgithubuser.data.dao.UserDetailDAO
import com.fly.sample_showgithubuser.extension.KEY_LIGIN
import com.fly.sample_showgithubuser.extension.restoreInstanceString
import com.fly.sample_showgithubuser.extension.saveInstance
import com.fly.sample_showgithubuser.footer_lrecyclerView.LRecyclerFooterModel
import com.fly.sample_showgithubuser.presenter.UserListPresenter
import com.fly.sample_showgithubuser.repository.UserDetailRepository
import com.fly.sample_showgithubuser.repository.UserListRepository
import com.fly.sample_showgithubuser.viewblock.UserDetailViewBlock

class UserDetailActivity : AppCompatActivity() {

    private var login: String? = null
    private val repository by lazy { UserDetailRepository() }
    private lateinit var viewBlock: UserDetailViewBlock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        getIntentValue()
        initModel()
        getUserDetail()
    }

    fun onClick(view: View){
        when(view.id){
            R.id.cl_delete -> finish()
        }
    }

    private fun getUserDetail(){
        login?.let {
            repository.getUserDetail(UserDetailDAO(it),{userDetail ->
                with(userDetail){
                    this?.avatar_url?.let { viewBlock.setUserPhoto(it) }
                    this?.name?.let { viewBlock.setName(it) }
                    this?.bio?.let { viewBlock.setBio(it) }
                    this?.login?.let { viewBlock.setLoginName(it) }
                    if(this?.site_admin?:false) viewBlock.showAdminBadge()
                    this?.location?.let { viewBlock.setLocation(it) }
                    this?.blog?.let { viewBlock.setBlog(it) }
                }
            },::apiFail)
        }
    }

    private fun apiFail(errMessage: String) {
        Toast.makeText(this, errMessage, Toast.LENGTH_LONG).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        login?.let { outState.saveInstance(KEY_LIGIN, it) }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        login = savedInstanceState.restoreInstanceString(KEY_LIGIN)
    }

    private fun initModel() {
        viewBlock = UserDetailViewBlock(this)
    }

    private fun getIntentValue() {
        login = intent.getStringExtra(KEY_LIGIN)
    }
}