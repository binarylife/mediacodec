package com.fengbei.car.mediacodec.mediacodec.extractor;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * author: beifeng.
 * date:   On 2020/3/17
 * 音视频提取器
 */
public class MMExtractor {

    private final static String TAG = MMExtractor.class.getSimpleName();

    private MediaExtractor mediaExtractor;
    private int mVideoTrack = -1;
    private int mAudioTrack = -1;
    private long sampleTime;
    private long mStartPos;
    /**
     * 当前帧时间戳
     */
    private long mCurSampleTime = 0;

    /**
     * 当前帧时间flag
     */
    private int mCurSampleFlag = 0;

    public MMExtractor(String path) {
        mediaExtractor = new MediaExtractor();
        try {
            mediaExtractor.setDataSource(path);
            Log.d(TAG, "mediaExtractor.setDataSource ： " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取视频格式参数
     */

    public MediaFormat getVideoFormat() {
        //  获取视频的格式
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
            String mime = trackFormat.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video/")) {
                mVideoTrack = i;
                break;
            }
        }

        if (mVideoTrack >= 0) {
            return mediaExtractor.getTrackFormat(mVideoTrack);
        } else {
            return null;
        }
    }

    /**
     * 获取音频频格式参数
     */
    public MediaFormat getAudioFormat() {
        //  获取视频的格式
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
            String mime = trackFormat.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
                Log.d(TAG, "getAudioFormat ： " + mime);
                mAudioTrack = i;
                break;
            }
        }

        if (mAudioTrack >= 0) {
            return mediaExtractor.getTrackFormat(mAudioTrack);
        } else {
            return null;
        }
    }

    /**
     * 读取视频数据
     */

    int readBuffer(ByteBuffer buffer) {
        //  提取数据
        buffer.clear();
        selectSourceTrack();
        int readSampleCount = mediaExtractor.readSampleData(buffer, 0);
        if (readSampleCount < 0) {
            return -1;
        }

        mCurSampleTime = mediaExtractor.getSampleTime();
        mCurSampleFlag = mediaExtractor.getSampleFlags();
        mediaExtractor.advance();
        return readSampleCount;
    }

    public void selectSourceTrack() {
        if (mVideoTrack >= 0) {
            mediaExtractor.selectTrack(mVideoTrack);
        } else if (mAudioTrack >= 0) {
            mediaExtractor.selectTrack(mAudioTrack);
        }
    }


    /**
     * 跳转至指定位置
     *
     * @param pos
     * @return
     */
    public long seek(long pos) {
        mediaExtractor.seekTo(pos, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
        return mediaExtractor.getSampleTime();
    }

    /**
     * 停止读取数据
     */

    void stop() {
        mediaExtractor.release();
        mediaExtractor = null;
    }

    int getVideoTrack() {
        return mVideoTrack;
    }

    int getAudiorack() {
        return mAudioTrack;
    }

    void setStartPos(long pos) {
        mStartPos = pos;
    }

    long getCurrentTimestamp() {
        return mCurSampleTime;
    }


    int getCurSampleFlag() {
        return mCurSampleFlag;
    }

}
