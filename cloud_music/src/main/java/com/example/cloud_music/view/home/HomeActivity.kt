package com.example.cloud_music.view.home

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.view.GravityCompat
import com.example.cloud_music.R
import com.example.cloud_music.databinding.ActivityHomeBinding
import com.example.cloud_music.model.CHANNEL
import com.example.cloud_music.model.login.LoginEvent
import com.example.cloud_music.utils.UserManager
import com.example.cloud_music.view.login.LoginActivity
import com.example.cloud_music.view.home.adapter.HomePagerAdapter
import com.example.lib_commin_ui.base.BaseActivity
import com.example.lib_image_loader.app.ImageLoaderManager
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class HomeActivity :  BaseActivity(),View.OnClickListener {
    //activity_home的ViewBinding
    private lateinit var binding: ActivityHomeBinding
    //指定首页要出现的卡片
    private val CHANNELS = arrayOf(
        CHANNEL.MY,
        CHANNEL.DISCORY,
        CHANNEL.FRIEND
    )

    private lateinit var mAdapter: HomePagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        EventBus.getDefault().register(this)
        setContentView(binding.root)
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


        }
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
}