package com.example.cloud_music.view.friend.adapter

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.cloud_music.R
import com.example.cloud_music.model.friend.FriendBodyValue
import com.example.cloud_music.utils.UserManager
import com.example.cloud_music.view.login.LoginActivity
import com.example.lib_audio.app.AudioHelper
import com.example.lib_commin_ui.MultiImageViewLayout
import com.example.lib_commin_ui.recyclerview.MultiItemTypeAdapter
import com.example.lib_commin_ui.recyclerview.base.ItemViewDelegate
import com.example.lib_commin_ui.recyclerview.base.ViewHolder
import com.example.lib_image_loader.app.ImageLoaderManager

class FriendRecyclerAdapter : MultiItemTypeAdapter<FriendBodyValue> {

    val MUSIC_TYPE = 0x01 //音乐类型
    val VIDEO_TYPE = 0x02 //视频类型

    constructor(context: Context, datas: List<FriendBodyValue>) : super(context, datas) {
        addItemViewDelegate(MUSIC_TYPE, MusicItemDelegate())
        addItemViewDelegate(VIDEO_TYPE, VideoItemDelegate())
    }

    inner class MusicItemDelegate : ItemViewDelegate<FriendBodyValue> {
        override fun getItemViewLayoutId(): Int {
            return R.layout.item_friend_list_picture_layout
        }

        override fun isForViewType(item: FriendBodyValue, position: Int): Boolean {
            return item.type == MUSIC_TYPE
        }

        override fun convert(holder: ViewHolder, recommandBodyValue: FriendBodyValue, position: Int) {
            holder.run {
                setText(R.id.name_view, "${recommandBodyValue.name}  分享单曲:")
                setText(R.id.fansi_view, "${recommandBodyValue.fans} 粉丝")
                setText(R.id.text_view, recommandBodyValue.text)
                setText(R.id.zan_view, recommandBodyValue.zan)
                setText(R.id.message_view, recommandBodyValue.msg)
                setText(R.id.audio_name_view, recommandBodyValue.audioBean.name)
                setText(R.id.audio_author_view, recommandBodyValue.audioBean.album)
                setOnClickListener(R.id.album_layout) {
                    //调用播放器装饰类
                    AudioHelper.addAudio(mContext as Activity, recommandBodyValue.audioBean)
                }
                setOnClickListener(R.id.guanzhu_view) {
                    if (!UserManager.hasLogined) {
                        //goto login
                        LoginActivity.start(mContext)
                    }
                }
            }

            val avatar = holder.getView<ImageView>(R.id.photo_view)
            ImageLoaderManager.getInstance().displayImageForCircle(avatar, recommandBodyValue.avatr)
            //这个为啥报错
//            val albumPicView = holder.getView<ImageView>(R.id.album_view)
//            ImageLoaderManager.getInstance()
//                    .displayImageForView(albumPicView, recommandBodyValue.audioBean.albumPic)

            val imageViewLayout = holder.getView<MultiImageViewLayout>(R.id.image_layout)
            imageViewLayout.setList(recommandBodyValue.pics)
        }
    }

    inner class VideoItemDelegate : ItemViewDelegate<FriendBodyValue> {
        override fun getItemViewLayoutId(): Int {
            return R.layout.item_friend_list_video_layout
        }

        override fun isForViewType(item: FriendBodyValue, position: Int): Boolean {
            return item.type == VIDEO_TYPE
        }

        override fun convert(holder: ViewHolder, recommandBodyValue: FriendBodyValue, position: Int) {
            val videoGroup = holder.getView<RelativeLayout>(R.id.video_layout)
            //video播放
            //VideoAdContext(videoGroup, recommandBodyValue.videoUrl)
            holder.run {
                setText(R.id.fansi_view, "${recommandBodyValue.fans}粉丝")
                setText(R.id.name_view, "${recommandBodyValue.name} 分享视频")
                setText(R.id.text_view, recommandBodyValue.text)
                setOnClickListener(R.id.guanzhu_view) {
                    if (!UserManager.hasLogined) {
                        //goto login
                        LoginActivity.start(mContext)
                    }
                }
            }

            val avatar = holder.getView<ImageView>(R.id.photo_view)
            ImageLoaderManager.getInstance().displayImageForCircle(avatar, recommandBodyValue.avatr)
        }
    }
}