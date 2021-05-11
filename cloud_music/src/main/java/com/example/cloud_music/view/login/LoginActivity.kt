package com.example.cloud_music.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.cloud_music.databinding.ActivityLoginLayoutBinding
import com.example.cloud_music.view.login.inter.IUserLoginView
import com.example.cloud_music.view.login.presenter.UserLoginPresenter
import com.example.lib_commin_ui.base.BaseActivity


/**
 * 登录页面
 */
class LoginActivity : BaseActivity(),IUserLoginView{
    lateinit var binding:ActivityLoginLayoutBinding


    private val mUserLoginPresenter: UserLoginPresenter.UserLoginPresenter = UserLoginPresenter.UserLoginPresenter(this)

    //伴生对象
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginView.setOnClickListener {
            mUserLoginPresenter.login(getUserName(), getPassword())
        }
    }

    override fun getUserName(): String {
        return "18734924592"
    }

    override fun getPassword(): String {
        return "999999q"
    }

    override fun finishActivity() {
        this.finish()
    }

    override fun showLoginFailedView() {
    }

    override fun showLoadingView() {
    }

    override fun hideLoadingView() {
    }
}