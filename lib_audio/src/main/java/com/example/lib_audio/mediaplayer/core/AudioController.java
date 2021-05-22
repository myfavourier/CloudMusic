package com.example.lib_audio.mediaplayer.core;


import com.example.lib_audio.mediaplayer.exception.AudioQueueEmptyException;
import com.example.lib_audio.mediaplayer.model.AudioBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Random;

/**
 * 控制播放逻辑
 */
public class AudioController {
    /**
     * 播放方式
     */
    public enum PlayMode {
        /**
         * 列表循环
         */
        LOOP,
        /**
         * 随机
         */
        RANDOM,
        /**
         * 单曲循环
         */
        REPEAT
    }

    public static AudioController getInstance() {
        return AudioController.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static AudioController instance = new AudioController();
    }

    private AudioPlayer mAudioPlayer;//核心播放器
    private ArrayList<AudioBean> mQueue;//歌曲队列
    private int mQueueIndex;//当期歌曲索引
    private PlayMode mPlayMode;//循环模式

    private AudioController(){
        mAudioPlayer = new AudioPlayer();
        mQueue = new ArrayList<>();
        mQueueIndex = 0;
        mPlayMode = PlayMode.LOOP;
    }

    public ArrayList<AudioBean> getQueue(){
        return mQueue == null ? new ArrayList<AudioBean>() : mQueue;
    }

    /**
     * 对外提供设置播放队列
     * @param queue
     */
    public void setQueue(ArrayList<AudioBean> queue){
        this.setQueue(queue,0);
    }

    public void setQueue(ArrayList<AudioBean> queue,int queueIndex){
        mQueue.addAll(queue);
        mQueueIndex = queueIndex;
    }

    /**
     * 添加单一歌曲到指定位置
     * @param bean
     * @param index
     */
    public void addAudio(AudioBean bean,int index){
        if(mQueue == null){
            throw new AudioQueueEmptyException("当前播放队列为空");
        }
        int query = queryAudio(bean);
        if(query <= -1){
            //未添加过
            addCustomAudio(index,bean);
            setPlayIndex(index);
        }else{
            AudioBean currentBean = getNowPlaying();
            if (!currentBean.id.equals(bean.id)) {
                //已经添加过且不在播放中
                setPlayIndex(query);
            }
        }
    }

    private void addCustomAudio(int index, AudioBean bean) {
    }

    private int queryAudio(AudioBean bean) {
        return 0;
    }

    public void addAudio(AudioBean bean){
        this.addAudio(bean,0);
    }

    public PlayMode getPLayMode(){
        return mPlayMode;
    }

    /**
     * 对外提供设置播放模式
     * @param playMode
     */
    public void setPlayMode(PlayMode playMode){
        mPlayMode = playMode;
    }

    public void setPlayIndex(int index) {
        if (mQueue == null) {
            throw new AudioQueueEmptyException("当前播放队列为空,请先设置播放队列.");
        }
        mQueueIndex = index;
        play();
    }


    public int getPlayIndex(){
        return mQueueIndex;
    }

    private AudioBean getNowPlaying(){
        return getPlaying();
    }

    private AudioBean getPlaying() {
        if (mQueue != null && !mQueue.isEmpty() && mQueueIndex >= 0 && mQueueIndex < mQueue.size()) {
            return mQueue.get(mQueueIndex);
        } else {
            throw new AudioQueueEmptyException("当前播放队列为空,请先设置播放队列.");
        }
    }

    private AudioBean getNextPlaying(){
        switch (mPlayMode){
            case LOOP:
                mQueueIndex = (mQueueIndex + 1) % mQueue.size();
                break;
            case RANDOM:
                mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
                break;
            case REPEAT:
                break;
        }
        return getPlaying();
    }

    private AudioBean getPreviousPlaying(){
        switch (mPlayMode){
            case LOOP:
                mQueueIndex = (mQueueIndex - 1) % mQueue.size();
                break;
            case RANDOM:
                mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
                break;
            case REPEAT:
                break;
        }
        return getPlaying();
    }
    /**
     * 对外提供的play方法
     */
    public void play() {
        AudioBean bean = getNowPlaying();
        mAudioPlayer.load(bean);
    }

    public void pause(){
        mAudioPlayer.pause();
    }

    public void resume(){
        mAudioPlayer.resume();
    }

    public void release(){
        mAudioPlayer.release();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 播放下一首歌曲
     */
    public void next(){
        AudioBean bean = getNextPlaying();
        mAudioPlayer.load(bean);
    }
    public void previous(){
        AudioBean bean = getPreviousPlaying();
        mAudioPlayer.load(bean);
    }

    /**
     * 自动切换播放/暂停
     */
    public void playOrPause(){
        if(isStartState()){
            pause();
        }
        if(isPauseState()){
            resume();
        }
    }



    public int getQueueIndex() {
        return mQueueIndex;
    }

    /**
     * 对外提供是否播放中状态
     */
    public boolean isStartState() {
        return CustomMediaPlayer.Status.STARTED == getStatus();
    }

    /**
     * 对外提提供是否暂停状态
     */
    public boolean isPauseState() {
        return CustomMediaPlayer.Status.PAUSED == getStatus();
    }

    /**
     * 获取播放器当前状态
     * @return
     */
    private CustomMediaPlayer.Status getStatus(){
        return mAudioPlayer.getStatus();
    }

}
