package com.fengbei.car.mediacodec.mediacodec.decoder;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.fengbei.car.mediacodec.mediacodec.BaseDecoder;
import com.fengbei.car.mediacodec.mediacodec.IExtractor;
import com.fengbei.car.mediacodec.mediacodec.extractor.VideoExtractor;

import java.nio.ByteBuffer;

/**
 * author: beifeng.
 * date:   On 2020/3/19
 * 视频解码器
 */
public class VideoDecoder extends BaseDecoder {
    private String TAG = "VideoDecoder";
    private SurfaceView mSurfaceView;
    private Surface mSurface;

    /**
     * @param filePath
     */
    public VideoDecoder(String filePath, SurfaceView surfaceView, Surface surface) {
        super(filePath);
        mSurfaceView = surfaceView;
        mSurface = surface;
    }


    @Override
    protected boolean check() {
        if (mSurface == null && mSurfaceView == null) {
            Log.w(TAG, "SurfaceView和Surface都为空，至少需要一个不为空");
            mStateListener.decoderError(this, "显示器为空");
            return false;
        }
        return true;
    }

    @Override
    protected IExtractor initExtractor(String path) {
        Log.d(TAG, "initExtractor :");
        return new VideoExtractor(path);
    }

    @Override
    protected void initSpecParams(MediaFormat mediaFormat) {

    }

    @Override
    protected boolean configCodec(final MediaCodec mediaCodec, final MediaFormat format) {
        Log.d(TAG, "configCodec :");

        if (mSurface != null) {
            mCodec.configure(format, mSurface, null, 0);
            Log.d(TAG, "notifyDecode :");
            notifyDecode();
        } else if (mSurfaceView.getHolder().getSurface() != null) {
            Log.d(TAG, "notifyDecode 01:");
            mSurface = mSurfaceView.getHolder().getSurface();
            configCodec(mediaCodec, format);
        } else {
            mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback2() {
                @Override
                public void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {

                }

                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    Log.d(TAG, "notifyDecode 02:");
                    mSurface = surfaceHolder.getSurface();
                    configCodec(mediaCodec, format);
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                }
            });
            return false;
        }
        return true;
    }

    @Override
    protected boolean initRender() {
        Log.d(TAG, "initRender :");
        return true;
    }

    @Override
    protected void render(ByteBuffer outputBuffer, MediaCodec.BufferInfo bufferInfo) {

    }

    @Override
    protected void doneDecode() {

    }
}
