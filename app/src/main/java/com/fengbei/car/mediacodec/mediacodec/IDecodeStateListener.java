package com.fengbei.car.mediacodec.mediacodec;

import com.fengbei.car.mediacodec.Frame;

/**
 * author: beifeng.
 * date:   On 2020/3/11
 * 解码状态回调接口
 */
public interface IDecodeStateListener {

    void decoderPrepare(BaseDecoder decodeJob);

    void decoderReady(BaseDecoder decodeJob);

    void decoderRunning(BaseDecoder decodeJob);

    void decoderPause(BaseDecoder decodeJob);

    /**
     * 解码一帧数据
     *
     * @param decodeJob
     */
    void decodeOneFrame(BaseDecoder decodeJob, Frame frame);

    void decoderFinish(BaseDecoder decodeJob);

    void decoderDestroy(BaseDecoder decodeJob);

    void decoderError(BaseDecoder decodeJob, String msg);


}
