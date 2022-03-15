package com.fly.sample_showgithubuser.repository

import com.fly.sample_showgithubuser.data.dao.UserListDAO
import com.fly.sample_showgithubuser.data.vo.UserListVO
import com.fly.sample_showgithubuser.extension.jsonToValue
import com.fly.sample_showgithubuser.extension.parseJsonArrayWithGson
import com.fly.sample_showgithubuser.network.ApiManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListRepository {

    fun getUsers(
        userListDAO: UserListDAO,
        apiSuccess: (List<UserListVO>?) -> Unit,
        apiFail: (String) -> Unit
    ) {
        val call = ApiManager.apiService.getUsers(userListDAO.since, userListDAO.pageNumber)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response == null) {
                    apiFail("response null")
                } else {
                    apiSuccess(response.body().string().parseJsonArrayWithGson())
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                apiFail(t?.message ?: "no message")
            }
        })
    }
}