package com.fengbei.car.mediacodec.mediacodec;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * author: beifeng.
 * date:   On 2020/3/11
 * 定义基础解码器BaseDecoder。
 */
public abstract class BaseDecoder implements IDecoder {

    private static String TAG = "BaseDecoder";
    //-------------线程相关------------------------
    /**
     * 解码器是否运行
     */
    private boolean mIsRuning = true;

    /**
     * 线程等待锁
     */
    private Object mLocke = new Object();

    /**
     * 是否可以进入解码
     */
    private boolean mReadyForDecode = false;

    //-------------解码相关------------------------

    /**
     * 音视频解码器
     */
    private MediaCodec mCodec;

    /**
     * 音视频数据读取器
     */

    private IExtractor mExtractor;

    /**
     * 解码输入缓存区
     */

    private ByteBuffer[] mInputBuffers;


    /**
     * 解码输出缓存区
     */

    private ByteBuffer[] mOutPutBuffers;


    /**
     * 解码数据信息
     */

    private MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();

    /**
     * 解码状态
     */
    private DecodeState mState = DecodeState.STOP;

    /**
     * 解码状态监听
     */

    private IDecodeStateListener mStateListener;


    /**
     * 数据流是否结束
     */

    private boolean mIsEos;

    private int mVideoWidth;

    private int mVideoHeight;


    private long mDuration;
    private long mStartPos;
    private long mEndPos;
    private String mFilePath;


    /**
     * 开始解码时间，用于音视频同步
     */
    private long mStartTimeForSync = -1l;

    /**
     * 是否需要音视频渲染同步
     */

    private boolean mSyncRender = true;


    /**
     *
     */

    public BaseDecoder(String filePath) {
        mFilePath = filePath;
    }

    /**
     * 子类的参数校验
     *
     * @return
     */
    abstract boolean check();

    /**
     * 初始化音视频流提取器
     *
     * @param path
     * @return
     */
    abstract IExtractor initExtractor(String path);

    /**
     * 初始化子类特有的参数
     */
    abstract void initSpecParams(MediaFormat mediaFormat);

    /**
     * 配置解码器
     */

    abstract boolean configCodec(MediaCodec mediaCodec, MediaFormat format);

    /**
     * 初始化渲染器
     */

    abstract boolean initRender();

    /**
     * 渲染
     */
    abstract void render(ByteBuffer outputBuffer, MediaCodec.BufferInfo bufferInfo);


    /**
     * 结束解码
     */
    abstract void doneDecode();

    /**
     * 关键解码流程
     */
    @Override
    public void run() {
        mState = DecodeState.START;
        mStateListener.decoderPrepare(this);

        // 1.初始胡，启动解码器
        if (!init()) {
            return;
        }


        while (mIsRuning) {

            if (mState != DecodeState.START && mState != DecodeState.DECODING &&
                    mState != DecodeState.SEEKING) {
                waitDecode();
            }
            if (!mIsRuning || mState == DecodeState.STOP) {
                mIsRuning = false;
                break;
            }

            //  如果数据没有解码完毕，将数据推入解码器
            if (!mIsEos) {
                // 2. 将原始数据压入解码器
                mIsEos = pushBufferToDecoder();
            }

            // 3.将解码好的数据从缓冲区拉出来

            int index = pullBufferFromDecoder();

            if (index >= 0) {
                //  4 .开启渲染
                render(mOutPutBuffers[index], mBufferInfo);

                //  5.释放输出缓冲区
                mCodec.releaseOutputBuffer(index, true);
                if (mState == DecodeState.START) {
                    mState = DecodeState.PAUSE;
                }

                //  6.判断解码是否完成
                if (mBufferInfo.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                    mState = DecodeState.FINISH;
                    mStateListener.decoderFinish(this);
                }
            }
        }

        doneDecode();
        //  7 释放资源
        release();
    }

    private boolean init() {

        //  1.校验目标音视频文件的合法性
        if (mFilePath.isEmpty() || !new File(mFilePath).exists()) {
            Log.w(TAG, "文件路径为空");
            mStateListener.decoderError(this, "文件路径为空");
            return false;
        }

        //  检查子类参数完整性
        if (!check()) {
            return false;
        }

        //  2.  初始化数据提取器
        mExtractor = initExtractor(mFilePath);

        if (mExtractor != null || mExtractor.getFormat() == null) {
            Log.w(TAG, "无法解析文件");
            return false;
        }

        //  3. 初始化参数
        if (!initParams()) {
            return false;
        }

        //  4. 初始化渲染器

        if (!initRender()) {
            return false;
        }

        //  5. 初始化解码器
        if (!initConfig()) {
            return false;
        }

        return true;
    }

    private boolean initConfig() {
        try {
            //  1.根据音视频的编码格式来初始化解码器
            MediaFormat format = mExtractor.getFormat();
            String type = format.getString(MediaFormat.KEY_MIME);
            mCodec = MediaCodec.createDecoderByType(type);
            mCodec = MediaCodec.cre(type);

            //  2.配置解码器
            if (!configCodec(mCodec, format)) {
                waitDecode();
            }

            //  3.启动解码器
            mCodec.start();

            //  4.获取解码器缓冲区
            mInputBuffers = mCodec.getInputBuffers();
            mOutPutBuffers = mCodec.getOutputBuffers();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 未初始化完成解码线程进入等待阶段
     */
    private void waitDecode() {
        if (mState == DecodeState.PAUSE) {
            mStateListener.decoderPause(this);
        }
        synchronized (mLocke) {
            try {
                mLocke.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通知解码线程继续运行
     *
     * @return
     */

    protected void notifyDecode() {
        synchronized (mLocke) {
            mLocke.notifyAll();
        }

        if (mState == DecodeState.DECODING) {
            mStateListener.decoderRunning(this);
        }
    }

    private boolean initParams() {
        try {
            MediaFormat format = mExtractor.getFormat();
            mDuration = format.getLong(MediaFormat.KEY_DURATION);
            if (mEndPos == 0) {
                mEndPos = mDuration;
            }
            initSpecParams(format);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    /**
     * 将数据压入解码器输入缓冲
     */

    private boolean pushBufferToDecoder() {
        int dequeueInputBuffer = mCodec.dequeueInputBuffer(2000);
        boolean isEndofStream = false;
        if (dequeueInputBuffer >= 0) {
            ByteBuffer mInputBuffer = mInputBuffers[dequeueInputBuffer];
            int simpleSize = mExtractor.readBuffer(mInputBuffer);
            if (simpleSize < 0) {
                //  数据已经取完了 压入结束标签
                mCodec.queueInputBuffer(dequeueInputBuffer, 0, 0, mExtractor.getCurrentTimestamp(), MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                isEndofStream = true;
            } else {
                //  将数据压入缓冲器
                mCodec.queueInputBuffer(dequeueInputBuffer, 0, simpleSize, mExtractor.getCurrentTimestamp(), 0);

            }

        }
        return isEndofStream;
    }


    /**
     * 将解码好的数据从缓冲区拉出来
     */
    private int pullBufferFromDecoder() {
        int index = mCodec.dequeueOutputBuffer(mBufferInfo, 1000);
        switch (index) {
            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                break;
            case MediaCodec.INFO_TRY_AGAIN_LATER:
                break;
            case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                mOutPutBuffers = mCodec.getOutputBuffers();
                break;
            default:
                return index;
        }
        return -1;
    }

    /**
     * 解码完成后释放资源
     */
    private void release() {

        try {
            mState = DecodeState.STOP;
            mIsEos = false;
            mExtractor.stop();
            mCodec.stop();
            mCodec.release();
            mStateListener.decoderDestroy(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}


