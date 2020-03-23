package com.fengbei.car.mediacodec.mediacodec.extractor;

import android.media.MediaFormat;

import com.fengbei.car.mediacodec.mediacodec.IExtractor;

import java.nio.ByteBuffer;

/**
 * author: beifeng.
 * date:   On 2020/3/19
 * 视频提取器
 */
public class VideoExtractor implements IExtractor {


    private String mPath;

    private MMExtractor mmExtractor;

    public VideoExtractor(String path) {
        mPath = path;
        mmExtractor = new MMExtractor(mPath);
    }

    @Override
    public MediaFormat getFormat() {
        return mmExtractor.getVideoFormat();
    }

    @Override
    public int readBuffer(ByteBuffer byteBuffer) {
        return mmExtractor.readBuffer(byteBuffer);
    }

    @Override
    public long getCurrentTimestamp() {
        return mmExtractor.getCurrentTimestamp();
    }

    @Override
    public long seek(long pos) {
        return mmExtractor.seek(pos);
    }

    @Override
    public void setStartPos(long pos) {
        mmExtractor.setStartPos(pos);
    }

    @Override
    public void stop() {
        mmExtractor.stop();
    }

    @Override
    public int getSampleFlag() {
        return mmExtractor.getCurSampleFlag();
    }
}
