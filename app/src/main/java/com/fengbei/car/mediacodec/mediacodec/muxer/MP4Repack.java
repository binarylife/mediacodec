package com.fengbei.car.mediacodec.mediacodec.muxer;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

import com.fengbei.car.mediacodec.mediacodec.extractor.AudioExtractor;
import com.fengbei.car.mediacodec.mediacodec.extractor.VideoExtractor;
import com.fengbei.car.mediacodec.mediacodec.utils.AudioFileFunc;

import java.nio.ByteBuffer;

/**
 * author: beifeng.
 * date:   On 2020/3/23
 * 重新封装音视频数据
 */
public class MP4Repack {

    private String TAG = "MP4Repack";

    //  初始化音视频分离器
    private AudioExtractor mAExtractor = new AudioExtractor(AudioFileFunc.getVideoFilePath());
    private VideoExtractor mVExtractor = new VideoExtractor(AudioFileFunc.getVideoFilePath());

    //  初始化音视频封装器

    private MMuxer mMuxer = new MMuxer(AudioFileFunc.getFileBasePath() + "Final.mp4");


    /**
     * 启动封装器
     */
    public void star() {
        final MediaFormat audioFormat = mAExtractor.getFormat();
        final MediaFormat videoFormat = mVExtractor.getFormat();

        // 判断有无音频数据，若没有则 忽略音轨

        if (audioFormat != null) {
            mMuxer.addAudioTrack(audioFormat);
        } else {
            mMuxer.setNoAudio();
        }

        if (videoFormat != null) {
            mMuxer.addVideoTrack(videoFormat);
        } else {
            mMuxer.setNoVideo();
        }

        //  启动线程执行 封装

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "封装器开始执行...");

                //  申请空间
                ByteBuffer byteBuffer = ByteBuffer.allocate(500 * 1024);
                MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

                //  音频数据抽取 和 写入封装
                if (audioFormat != null) {
                    int size = mAExtractor.readBuffer(byteBuffer);
                    while (size > 0) {
                        bufferInfo.set(0, size, mAExtractor.getCurrentTimestamp(), mAExtractor.getSampleFlag());
                        mMuxer.writeAudioData(byteBuffer, bufferInfo);
                        size = mAExtractor.readBuffer(byteBuffer);
                        Log.i(TAG, "封装器音频数据  开始执行... " + size);
                    }
                }
                //  视频数据抽取 和 写入封装
                if (videoFormat != null) {
                    int size = mVExtractor.readBuffer(byteBuffer);
                    while (size > 0) {
                        bufferInfo.set(0, size, mVExtractor.getCurrentTimestamp(), mVExtractor.getSampleFlag());
                        mMuxer.writeVideoData(byteBuffer, bufferInfo);
                        size = mVExtractor.readBuffer(byteBuffer);
                        Log.i(TAG, "封装器视频数据  开始执行... " + size);
                    }
                }

                mAExtractor.stop();
                mVExtractor.stop();
                mMuxer.release();
                Log.i(TAG, "MP4 重打包完成");
            }
        }).start();
    }
}
