package com.fengbei.car.mediacodec.mediacodec.muxer;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * author: beifeng.
 * date:   On 2020/3/23
 * 1. 设置 音轨 视频轨
 * 2. 向MediaMuxer 写入音视频数据  byteBuffer
 */
public class MMuxer {
    private String TAG = "MMuxer";
    private MediaMuxer mediaMuxer;
    private int mVideotrackIndex;
    private int mAudiotrackIndex;
    private boolean mIsVideoTrackIsAdd;
    private boolean mIsAudioTrackIsAdd;
    private boolean mIsStart;

    public MMuxer(String path) {

        try {
            mediaMuxer = new MediaMuxer(path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 增加视频轨道
     *
     * @param mediaFormat
     */
    public void addVideoTrack(MediaFormat mediaFormat) {
        if (mediaMuxer != null) {
            mVideotrackIndex = mediaMuxer.addTrack(mediaFormat);
        }
        mIsVideoTrackIsAdd = true;
        //  开始封装
        startMuxer();
    }

    /**
     * 增加音频轨道
     *
     * @param mediaFormat
     */
    public void addAudioTrack(MediaFormat mediaFormat) {
        if (mediaMuxer != null) {
            mAudiotrackIndex = mediaMuxer.addTrack(mediaFormat);
        }
        mIsAudioTrackIsAdd = true;
        //  开始封装
        startMuxer();
    }

    /**
     * 忽略音轨
     */
    public void setNoAudio() {
        if (mIsAudioTrackIsAdd) {
            return;
        }
        mIsAudioTrackIsAdd = true;
        startMuxer();
    }


    /**
     * 忽略音轨
     */
    public void setNoVideo() {
        if (mIsVideoTrackIsAdd) {
            return;
        }
        mIsVideoTrackIsAdd = true;
        startMuxer();
    }

    public void startMuxer() {

        if (mIsVideoTrackIsAdd && mIsAudioTrackIsAdd) {
            if (mediaMuxer != null) {
                mediaMuxer.start();
                mIsStart = true;
                Log.i(TAG, "启动混合器，等待数据输入...");
            }
        }
    }


    public void writeVideoData(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {

        if (mIsStart) {
            mediaMuxer.writeSampleData(mVideotrackIndex, byteBuffer, bufferInfo);
            Log.i(TAG, "writeVideoData 数据写入...");
        }
    }

    public void writeAudioData(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {

        if (mIsStart) {
            mediaMuxer.writeSampleData(mAudiotrackIndex, byteBuffer, bufferInfo);
            Log.i(TAG, "writeVideoData 数据写入...");
        }
    }


    /**
     * 释放资源
     */

    void release() {
        mIsVideoTrackIsAdd = false;
        mIsVideoTrackIsAdd = false;
        mediaMuxer.stop();
        mediaMuxer.release();
        mediaMuxer = null;
        Log.i(TAG, "混合器退出...");
    }
}
