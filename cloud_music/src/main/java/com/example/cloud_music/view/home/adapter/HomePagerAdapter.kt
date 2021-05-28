package com.example.cloud_music.view.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cloud_music.model.*
import com.example.cloud_music.view.video.VideoFragment
import com.example.cloud_music.view.discory.DiscoryFragment
import com.example.cloud_music.view.friend.FriendFragment
import com.example.cloud_music.view.mine.MineFragment


/**
 * 首页框架adapter
 */
@Suppress("DEPRECATION")
class HomePagerAdapter : FragmentPagerAdapter {

    private var mList: Array<CHANNEL>? = null

    constructor(fragmentManager: FragmentManager, list: Array<CHANNEL>) : super(fragmentManager) {
        mList = list
    }

    override fun getItem(position: Int): Fragment {
        val type = mList!![position].value
        when (type) {
            MINE_ID -> return MineFragment.newInstance()
            DISCORY_ID -> return DiscoryFragment.newInstance()
            FRIEND_ID -> return FriendFragment.newInstance()
            VIDEO_ID -> return VideoFragment.newInstance()

        }
        throw IllegalStateException("position $position is invalid for this viewpager")


    }

    override fun getCount(): Int {
        return mList!!.size
    }
}
