package com.fengbei.car.mediacodec.mediacodec.decoder;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

import com.fengbei.car.mediacodec.mediacodec.BaseDecoder;
import com.fengbei.car.mediacodec.mediacodec.IExtractor;
import com.fengbei.car.mediacodec.mediacodec.extractor.AudioExtractor;

import java.nio.ByteBuffer;

/**
 * author: beifeng.
 * date:   On 2020/3/19
 */
public class AudioDecoder extends BaseDecoder {

    private String TAG = "AudioDecoder";
    private int mChannel = 1;
    private int mSimpleRate = -1;
    private int mPCMEncodeBit = AudioFormat.ENCODING_PCM_16BIT;
    private AudioTrack mAudioTrack;
    private short[] mAudioOutTempBuf;

    /**
     * @param filePath
     */
    public AudioDecoder(String filePath) {
        super(filePath);
    }

    @Override
    protected boolean check() {
        return true;
    }

    @Override
    protected IExtractor initExtractor(String path) {
        return new AudioExtractor(path);
    }

    @Override
    protected void initSpecParams(MediaFormat mediaFormat) {

        mChannel = mediaFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
        mSimpleRate = mediaFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);

        if (mediaFormat.containsKey(MediaFormat.KEY_PCM_ENCODING)) {
            mPCMEncodeBit = mediaFormat.getInteger(MediaFormat.KEY_PCM_ENCODING);
        } else {
            mPCMEncodeBit = AudioFormat.ENCODING_PCM_16BIT;
        }

        Log.d(TAG, "initSpecParams :" + " mChannel:" + mChannel + "mSimpleRate : " + mSimpleRate + "mPCMEncodeBit : " + mPCMEncodeBit);
    }

    @Override
    protected boolean configCodec(MediaCodec mediaCodec, MediaFormat format) {
        mediaCodec.configure(format, null, null, 0);
        return true;
    }

    @Override
    protected boolean initRender() {
        Log.d(TAG, "initRender :");
        int channel;
        if (mChannel == 1) {
            channel = AudioFormat.CHANNEL_OUT_MONO;
        } else {
            channel = AudioFormat.CHANNEL_OUT_STEREO;
        }
        //  获取最小缓冲区

        int minBufferSize = AudioTrack.getMinBufferSize(mSimpleRate, channel, mPCMEncodeBit);

        mAudioOutTempBuf = new short[minBufferSize/2];
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mSimpleRate, channel, mPCMEncodeBit, minBufferSize, AudioTrack.MODE_STREAM);
        mAudioTrack.play();
        return true;
    }

    @Override
    protected void render(ByteBuffer outputBuffer, MediaCodec.BufferInfo bufferInfo) {
        Log.d(TAG, "render :" + bufferInfo.toString());
        if (mAudioOutTempBuf.length < bufferInfo.size/2) {
            Log.d(TAG, "mAudioOutTempBuf.length < bufferInfo.size / 2 :");
            mAudioOutTempBuf = new short[bufferInfo.size/2];
        }
        outputBuffer.position(0);
        outputBuffer.asShortBuffer().get(mAudioOutTempBuf, 0, bufferInfo.size/2);
        mAudioTrack.write(mAudioOutTempBuf, 0, bufferInfo.size/2);
        Log.d(TAG, "write :" + mAudioOutTempBuf.length);
    }

    @Override
    protected void doneDecode() {
        mAudioTrack.stop();
        mAudioTrack.release();
    }
}
