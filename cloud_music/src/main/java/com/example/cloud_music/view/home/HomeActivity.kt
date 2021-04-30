package com.example.cloud_music.view.home

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cloud_music.databinding.ActivityHomeBinding
import com.example.cloud_music.model.CHANNEL
import com.example.cloud_music.view.home.adapter.HomePagerAdapter
import com.example.lib_commin_ui.base.BaseActivity
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView


class HomeActivity :  BaseActivity() {
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
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        //初始化adpater
        mAdapter = HomePagerAdapter(supportFragmentManager, CHANNELS)
        binding.viewpager.adapter = mAdapter
        initMagicIndicator()

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
}