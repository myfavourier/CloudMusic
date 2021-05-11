package com.example.cloud_music.view.login.inter

interface IUserLoginPresenter {

    fun login(name: String, password: String)
}

/**
 * UI层对外提供的方法
 */
interface IUserLoginView {

    fun getUserName(): String

    fun getPassword(): String

    fun finishActivity()

    fun showLoginFailedView()

    fun showLoadingView()

    fun hideLoadingView()
}