package com.fengbei.car.mediacodec.mediacodec;

import android.media.MediaFormat;

/**
 * author: beifeng.
 * date:   On 2020/3/11
 * 解码器接口
 */
public interface IDecoder extends Runnable {

    /**
     * 暂停解码
     */
    void pause();

    /**
     * 继续解码
     */
    void goOn();

    /**
     * 快进
     */

    long seekTo(long pos);

    /**
     * 停止解码
     */
    void stop();

    /**
     * 是否是正在解码
     *
     * @return
     */
    boolean isDecoding();

    /**
     * 是否正在快进
     *
     * @return
     */
    boolean isSeeking();

    /**
     * 是否正停止
     *
     * @return
     */
    boolean isStop();

    /**
     * 设置状态监听器
     */
    void setStateLinstener();

    /**
     * 获取视频宽度
     */
    int getWidth();

    /**
     * 获取视频高度
     */
    int getHeight();


    /**
     * 获取视频长度
     *
     * @return
     */
    long getDuration();

    /**
     * 获取视频的角度
     *
     * @return
     */
    int getRotationAngle();

    /**
     * 获取音视频对应的格式
     *
     * @return
     */
    MediaFormat getMediaFormat();


    /**
     * 获取音视频的媒体轨道
     *
     * @return
     */
    int getTrack();


    /**
     * 获取解码的文件路径
     */

    String getFilePath();


    /**
     * 获取当前帧的时间
     */
    long getCurTimeStamp();
}
