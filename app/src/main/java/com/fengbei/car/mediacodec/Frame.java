package com.fengbei.car.mediacodec;

import android.media.MediaCodec;

import java.nio.ByteBuffer;

/**
 * author: beifeng.
 * date:   On 2020/3/11
 * 一帧数据
 */
public class Frame {

    ByteBuffer byteBuffer;


    MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

    void setBufferInfo(MediaCodec.BufferInfo bufferInfo) {
        this.bufferInfo.set(bufferInfo.offset, bufferInfo.size, bufferInfo.presentationTimeUs, bufferInfo.flags);
    }
}
