package com.fly.sample_showgithubuser.extension

import com.fly.sample_showgithubuser.data.vo.UserListVO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken




fun String.jsonToValue(cd: Class<*>): Any? {
    var value: Any? = null
    try {
        value = Gson().fromJson(this, cd)
    } catch (e: Exception) {
        e.printStackTrace(System.out)
    }
    return value
}

fun String.parseJsonArrayWithGson(): List<UserListVO>? {
    var value: List<UserListVO>? = null
    try {
        value = Gson().fromJson(this, object : TypeToken<List<UserListVO>?>() {}.type)
    } catch (e: Exception) {
        e.printStackTrace(System.out)
    }
    return value
}
