package com.fly.sample_showgithubuser.presenter

import com.fly.sample_showgithubuser.contract.UserListContract
import com.fly.sample_showgithubuser.data.vo.UserListVO

class UserListPresenter(val view: UserListContract.View): UserListContract.Presenter {

    companion object{
        private val PAGE_NUMBER = 20
        private val MAX_DATA_NUMBER = 100
    }

    override fun checkPage(lastValue : UserListVO?, itemSize: Int) {
        when(itemSize){
            0 -> view.getPage(0,PAGE_NUMBER)
            MAX_DATA_NUMBER -> view.finishGetValue()
            else -> view.getPage(lastValue?.id?:0,PAGE_NUMBER)
        }
    }
}