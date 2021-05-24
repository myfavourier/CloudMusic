package com.example.cloud_music.application

import android.app.Application
import android.content.Context
import com.example.lib_audio.app.AudioHelper


class ImoocVoiceApplication : Application() {

    //伴身对象实现单例
    companion object {
        lateinit var mInstance: ImoocVoiceApplication
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        init()
    }


    private fun init() {
        //视频SDK初始化
//        VideoHelper.init(mInstance)
        //音频SDK初始化
        AudioHelper.init(mInstance)
        //分享SDK初始化
//        ShareManager.initSDK(mInstance)
//        //更新组件下载
//        UpdateHelper.init(mInstance)
//        //ARouter初始化
//        ARouter.init(mInstance)
    }
}