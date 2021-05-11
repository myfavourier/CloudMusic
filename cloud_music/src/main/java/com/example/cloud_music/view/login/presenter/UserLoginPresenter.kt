package com.example.cloud_music.view.login.presenter

import com.example.cloud_music.api.KRequestCenter
import com.example.cloud_music.api.MockData
import com.example.cloud_music.model.user.User
import com.example.cloud_music.model.login.LoginEvent
import com.example.cloud_music.utils.UserManager
import com.example.cloud_music.view.login.LoginActivity
import com.example.cloud_music.view.login.inter.IUserLoginPresenter
import com.example.cloud_music.view.login.inter.IUserLoginView
import com.example.lib_network.okhttp.listener.DisposeDataListener
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus

class UserLoginPresenter(loginActivity: LoginActivity) {
    class UserLoginPresenter constructor(userLoginView: IUserLoginView) : IUserLoginPresenter, DisposeDataListener {

        private val mUserLoginView: IUserLoginView = userLoginView

        override fun login(name: String, password: String) {
            mUserLoginView.showLoadingView()
            KRequestCenter.login(this)
        }

        override fun onSuccess(responseObj: Any) {
            mUserLoginView.hideLoadingView()
            //保存到单例对象中
            UserManager.mUser = (responseObj as User)
            //发送登陆Event
            EventBus.getDefault().post(LoginEvent())
            mUserLoginView.finishActivity()
        }

        override fun onFailure(reasonObj: Any) {
            mUserLoginView.hideLoadingView()
            //onSuccess(ResponseEntityToModule.parseJsonToModule(MockData.LOGIN_DATA, User::class.java))
            onSuccess(Gson().fromJson(MockData.LOGIN_DATA, User::class.java))
            mUserLoginView.showLoginFailedView()
        }
    }
}