package com.example.lib_network.okhttp.listener;
/**
 * @author yax
 * @function 监听下载进度
 */
public interface DisposeDownloadListener extends DisposeDataListener {
    void onProgress(int progress);
}
