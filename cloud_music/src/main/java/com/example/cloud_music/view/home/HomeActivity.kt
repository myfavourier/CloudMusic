package com.example.cloud_music.view.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.alibaba.android.arouter.launcher.ARouter
import com.example.cloud_music.R
import com.example.cloud_music.constant.Constant
import com.example.cloud_music.databinding.ActivityHomeBinding
import com.example.cloud_music.model.CHANNEL
import com.example.cloud_music.model.login.LoginEvent
import com.example.cloud_music.utils.UserManager
import com.example.cloud_music.utils.Utils
import com.example.cloud_music.view.login.LoginActivity
import com.example.cloud_music.view.home.adapter.HomePagerAdapter
import com.example.lib_audio.app.AudioHelper
import com.example.lib_audio.mediaplayer.core.AudioController
import com.example.lib_audio.mediaplayer.core.MusicService.startMusicService
import com.example.lib_audio.mediaplayer.model.AudioBean
import com.example.lib_commin_ui.base.BaseActivity
import com.example.lib_image_loader.app.ImageLoaderManager
import com.imooc.lib_update.app.UpdateHelper
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList


class HomeActivity :  BaseActivity(),View.OnClickListener {
    //activity_home的ViewBinding
    private lateinit var binding: ActivityHomeBinding
    //指定首页要出现的卡片
    private val CHANNELS = arrayOf(
        CHANNEL.MY,
        CHANNEL.DISCORY,
        CHANNEL.FRIEND
    )

    private val mReceiver: KotUpdateReceiver = KotUpdateReceiver()


    /*
    * data
    */
    private val mLists = ArrayList<AudioBean>()
    private lateinit var mAdapter: HomePagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        EventBus.getDefault().register(this)
        setContentView(binding.root)
        initData()
        initView()


    }

    private fun initView(){
        //初始化adpater
        mAdapter = HomePagerAdapter(supportFragmentManager, CHANNELS)
        binding.viewpager.adapter = mAdapter
        initMagicIndicator()
        //登录相关UI
        binding.unlogginlayout.setOnClickListener(this)
        binding.toggleview.setOnClickListener {
            binding.drawerlayout.openDrawer(GravityCompat.START)
        }

    }
    //初始化指示器
    private fun initMagicIndicator(){
        binding.magicindicator.setBackgroundColor(Color.WHITE)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return CHANNELS.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorTransitionPagerTitleView(context)
                simplePagerTitleView.run {
                    text = CHANNELS[index].key
                    textSize = 19f
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    normalColor = Color.parseColor("#999999")
                    selectedColor = Color.parseColor("#333333")
                    setOnClickListener { binding.viewpager.currentItem = index }
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                return null
            }

            override fun getTitleWeight(context: Context?, index: Int): Float {
                return 1.0f
            }
        }
        binding.magicindicator.navigator = commonNavigator
        ViewPagerHelper.bind(binding.magicindicator, binding.viewpager)
    }

    private fun initData() {
        mLists.run {
            add(AudioBean("100001", "http://sp-sycdn.kuwo.cn/resource/n2/85/58/433900159.mp3",
                "以你的名字喊我", "周杰伦", "七里香", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698076304&di=e6e99aa943b72ef57b97f0be3e0d2446&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201401%2F04%2F20140104170315_XdG38.jpeg",
                "4:30"))
            add(
                AudioBean("100002", "http://sq-sycdn.kuwo.cn/resource/n1/98/51/3777061809.mp3", "勇气",
                    "梁静茹", "勇气", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698193627&di=711751f16fefddbf4cbf71da7d8e6d66&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D213168965%2C1040740194%26fm%3D214%26gp%3D0.jpg",
                    "4:40"))
            add(
                AudioBean("100003", "http://sp-sycdn.kuwo.cn/resource/n2/52/80/2933081485.mp3", "灿烂如你",
                    "汪峰", "春天里", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698239736&di=3433a1d95c589e31a36dd7b4c176d13a&imgtype=0&src=http%3A%2F%2Fpic.zdface.com%2Fupload%2F201051814737725.jpg",
                    "3:20"))
            add(
                AudioBean("100004", "http://sr-sycdn.kuwo.cn/resource/n2/33/25/2629654819.mp3", "小情歌",
                    "五月天", "小幸运", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698289780&di=5146d48002250bf38acfb4c9b4bb6e4e&imgtype=0&src=http%3A%2F%2Fpic.baike.soso.com%2Fp%2F20131220%2Fbki-20131220170401-1254350944.jpg",
                    "2:45")
            )

        }
        AudioHelper.startMusicService(mLists)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.exitlayout -> {
                finish()
                System.exit(0)
            }

            R.id.unlogginlayout -> {
                if (!UserManager.hasLogined) {
                    LoginActivity.start(this)
                } else {
                    binding.drawerlayout.closeDrawer(Gravity.LEFT)
                }
            }

            R.id.toggleview -> {
                if (binding.drawerlayout.isDrawerOpen(Gravity.LEFT)) {
                    binding.drawerlayout.closeDrawer(Gravity.LEFT)
                } else {
                    binding.drawerlayout.openDrawer(Gravity.LEFT)
                }
            }

            R.id.home_qrcode -> {
                if (hasPermission(*Constant.HARDWEAR_CAMERA_PERMISSION)) {
                    doCameraPermission()
                } else {
                    requestPermission(Constant.HARDWEAR_CAMERA_CODE, *Constant.HARDWEAR_CAMERA_PERMISSION)
                }
            }

            R.id.home_music -> {
                goToMusic()
            }

            R.id.online_music_view -> {
                gotoWebView("https://www.imooc.com")
            }

            R.id.check_update_view -> {
                checkUpdate()
            }

        }
    }

    private fun goToMusic() {
        ARouter.getInstance().build(Constant.Router.ROUTER_MUSIC_ACTIVIYT).navigation()
    }

    private fun gotoWebView(url: String) {
        ARouter.getInstance()
                .build(Constant.Router.ROUTER_WEB_ACTIVIYT)
                .withString("url", url)
                .navigation()
    }

    //启动检查更新
    private fun checkUpdate() {
        UpdateHelper.checkUpdate(this)
    }

    private fun registerBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mReceiver, IntentFilter(UpdateHelper.UPDATE_ACTION))
    }

    private fun unRegisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    /**
     * 处理登陆事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: LoginEvent) {
        binding.unlogginlayout.visibility = View.GONE
        binding.avatrview.visibility = View.VISIBLE
        ImageLoaderManager.getInstance()
                .displayImageForCircle(binding.avatrview, UserManager.mUser?.data?.photoUrl)
    }

    /**
     * 接收Update发送的广播
     */
    inner class KotUpdateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //启动安装页面
            context.startActivity(
                Utils.getInstallApkIntent(context, intent.getStringExtra(UpdateHelper.UPDATE_FILE_KEY)))
        }
    }
}