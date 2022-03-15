package com.fly.sample_showgithubuser.contract

import com.fly.sample_showgithubuser.data.vo.UserListVO

interface UserListContract {

    interface View {
        fun getPage(since: Int,pageNumber: Int)
        fun finishGetValue()
    }

    interface Presenter {
        fun checkPage(lastValue : UserListVO?, itemSize: Int)
    }

}