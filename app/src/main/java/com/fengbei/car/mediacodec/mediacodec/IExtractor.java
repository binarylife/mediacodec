package com.fengbei.car.mediacodec.mediacodec;

import android.media.MediaFormat;

import java.nio.ByteBuffer;

/**
 * author: beifeng.
 * date:   On 2020/3/12
 * <p>
 * 音视频原始文件的缓冲读取器
 */
public interface IExtractor {

    /**
     * 获取音视频的格式
     *
     * @return
     */
    MediaFormat getFormat();

    /**
     * 读取音视频数据
     *
     * @param byteBuffer
     * @return
     */
    int readBuffer(ByteBuffer byteBuffer);

    /**
     * 获取当前帧的时间
     */

    long getCurrentTimestamp();


    /**
     * seek到指定的位置
     */

    long seek(long pos);


    /**
     * 设置起始的位置
     */

    void setStartPos(long pos);

    /**
     * 停止读取数据
     */
    void stop();

    /**
     * 设置起始的位置
     */

    int getSampleFlag();

}
