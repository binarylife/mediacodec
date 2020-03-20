package com.fengbei.car.mediacodec.mediacodec.extractor;

import android.media.MediaFormat;

import com.fengbei.car.mediacodec.mediacodec.IExtractor;

import java.nio.ByteBuffer;

/**
 * author: beifeng.
 * date:   On 2020/3/19
 * 视频提取器
 */
public class AudioExtractor implements IExtractor {


    private String mPath;

    private MMExtractor mmExtractor;

    public AudioExtractor(String path) {
        mPath = path;
        mmExtractor = new MMExtractor(path);
    }

    @Override
    public MediaFormat getFormat() {
        return mmExtractor.getAudioFormat();
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
}
