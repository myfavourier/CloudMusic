package com.example.lib_audio.mediaplayer.core;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import com.example.lib_audio.app.AudioHelper;
import com.example.lib_audio.mediaplayer.events.AudioCompleteEvent;
import com.example.lib_audio.mediaplayer.events.AudioErrorEvent;
import com.example.lib_audio.mediaplayer.events.AudioLoadEvent;
import com.example.lib_audio.mediaplayer.events.AudioPauseEvent;
import com.example.lib_audio.mediaplayer.events.AudioReleaserEvent;
import com.example.lib_audio.mediaplayer.events.AudioStartEvent;
import com.example.lib_audio.mediaplayer.model.AudioBean;

import org.greenrobot.eventbus.EventBus;

/**
 * 1.播放音频
 * 2.对外发送各种类型的事件
 */
public class AudioPlayer
    implements MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    AudioFocusManager.AudioFocusListener {

  private static final String TAG = "AudioPlayer";
  private static final int TIME_MSG = 0x01;
  private static final int TIME_INVAL = 100;

  //真正负责播放的核心MediaPlayer子类
  private CustomMediaPlayer mMediaPlayer;
  private WifiManager.WifiLock mWifiLock;
  //音频焦点监听器
  private AudioFocusManager mAudioFocusManager;
  private boolean isPausedByFocusLossTransient;

  private Handler mHandler = new Handler(Looper.getMainLooper()) {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case TIME_MSG:
          //暂停也要更新进度，防止UI不同步，只不过进度一直一样

          break;
      }
    }
  };

  /**
   * 完成唯一的mediaplayer初始化
   */
  public AudioPlayer() {
    init();
  }

  private void init() {
    mMediaPlayer = new CustomMediaPlayer();
    // 使用唤醒锁
    mMediaPlayer.setWakeMode(AudioHelper.getContext(), PowerManager.PARTIAL_WAKE_LOCK);
    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    mMediaPlayer.setOnCompletionListener(this);
    mMediaPlayer.setOnPreparedListener(this);
    mMediaPlayer.setOnBufferingUpdateListener(this);
    mMediaPlayer.setOnErrorListener(this);
    // 初始化wifi锁
    mWifiLock = ((WifiManager) AudioHelper.getContext()
            .getApplicationContext()
            .getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, TAG);
    // 初始化音频焦点管理器
    mAudioFocusManager = new AudioFocusManager(AudioHelper.getContext(), this);
  }

  /**
   * 对外提供加载方法
   * @param audioBean
   */
  public void load(AudioBean audioBean){
    try{
      //正常加载逻辑
      mMediaPlayer.reset();
      mMediaPlayer.setDataSource(audioBean.mUrl);
      mMediaPlayer.prepareAsync();
      //对外发送load事件
      EventBus.getDefault().post(new AudioLoadEvent(audioBean));
    }catch (Exception e){
      //对外发送error事件
      EventBus.getDefault().post(new AudioErrorEvent());
    }

  }

  /**
   * 内部开发播放
   */
  private void start(){
    if(mAudioFocusManager.requestAudioFocus()){
      Log.e(TAG, "获取音频焦点失败");
    }
    mMediaPlayer.start();
    mWifiLock.acquire();
    //对外发送start事件
    EventBus.getDefault().post(new AudioStartEvent());
  }

  /**
   * 对外提供暂停
   */
  public void pause(){
    if(getStatus() == CustomMediaPlayer.Status.STARTED){
      mMediaPlayer.pause();
      //释放wifi锁
      if(mWifiLock.isHeld()){
        mWifiLock.release();
      }
      //释放音频焦点
      if(mAudioFocusManager != null){
        mAudioFocusManager.abandonAudioFocus();
      }
      //发送暂停事件
      EventBus.getDefault().post(new AudioPauseEvent());
    }
  }

  /**
   * 对外提供恢复
   */
  public void resume(){
    if(getStatus() == CustomMediaPlayer.Status.PAUSED){
      //直接复用start方法
      start();
    }
  }

  /**
   * 清空播放器占用资源
   */
  public void release(){
    if(mMediaPlayer == null){
      return;
    }
    mMediaPlayer.release();
    mMediaPlayer = null;
    if(mAudioFocusManager != null){
      mAudioFocusManager.abandonAudioFocus();
    }
    if(mWifiLock.isHeld()){
      mWifiLock.release();
    }
    mWifiLock = null;
    mAudioFocusManager = null;
    //发送release事件
    EventBus.getDefault().post(new AudioReleaserEvent());
  }


  /**
   * 获取播放器当前状态
   * @return
   */
  public CustomMediaPlayer.Status getStatus(){
    if(mMediaPlayer != null){
      return mMediaPlayer.getState();
    }
    return CustomMediaPlayer.Status.STOPPED;
  }


  @Override
  public void onBufferingUpdate(MediaPlayer mp, int percent) {
    //缓存进度回调

  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    //播放完毕回调
    EventBus.getDefault().post(new AudioCompleteEvent());

  }

  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    //播放出错回调
    EventBus.getDefault().post(new AudioErrorEvent());
    return true;
  }

  @Override
  public void onPrepared(MediaPlayer mp) {
    //准备完毕
    start();
  }


  @Override
  public void audioFocusGrant() {
    //再次获得音频焦点
    setVolumn(1.0f,1.0f);
    if(isPausedByFocusLossTransient){
      resume();
    }
    isPausedByFocusLossTransient = false;
  }

  //设置音量
  private void setVolumn(float leftVol, float rightVol) {
    if(mMediaPlayer != null){
      mMediaPlayer.setVolume(leftVol, rightVol);
    }
  }

  @Override
  public void audioFocusLoss() {
    //永久失去焦点
    pause();

  }

  @Override
  public void audioFocusLossTransient() {
    //短暂性失去焦点
    pause();
    isPausedByFocusLossTransient = true;

  }

  @Override
  public void audioFocusLossDuck() {
    //瞬间失去焦点
    setVolumn(0.5f,0.5f);
  }
}

