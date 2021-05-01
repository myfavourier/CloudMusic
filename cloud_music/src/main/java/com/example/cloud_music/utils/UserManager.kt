package com.example.cloud_music.utils;

import com.example.cloud_music.model.User
import java.net.UnknownServiceException

/*
 * 用户管理类，单例对象
 */
object UserManager {

    var mUser: User? = null

    //计算属性
    var hasLogined = false
        get() = this.mUser != null

    /**
     * 保存用户信息到内存
     */
    fun saveUser(user: User){
        mUser = user
        saveLocal(user)
    }

    /**
     * 持久化用户信息
     */
    fun saveLocal(user: User){

    }

    fun getUser(): User? {
        return mUser
    }

    /**
     * 从本地获取
     */
    fun getLocal(): User? {
        return null
    }
}