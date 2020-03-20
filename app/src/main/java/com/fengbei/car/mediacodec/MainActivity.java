package com.fengbei.car.mediacodec;

import android.app.Activity;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.fengbei.car.mediacodec.mediacodec.decoder.AudioDecoder;
import com.fengbei.car.mediacodec.mediacodec.decoder.VideoDecoder;
import com.fengbei.car.mediacodec.mediacodec.utils.AudioFileFunc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private Button mFlacButton;
    private Button mWavButton;
    private Button mPcmButton;
    private Button mStartButton;
    private Button mStopButton;
    private SurfaceView mSurfaceView;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonStartRecording:
                    tryStart();
                    break;
                case R.id.buttonPauseRecording:
                    pause();
                    break;
                case R.id.buttonPlayRecording:
//                    play();
                    break;
                case R.id.buttonPauseFlac:
//                    flac();
                    break;
                case R.id.buttonPausePcm:
//                    pcm();
                    break;
                case R.id.buttonEncodeWav:
//                    wav();
                    break;
                default:
                    break;
            }
        }
    };


    private VideoDecoder videoDecoder;
    private AudioDecoder audioDecoder;
    private ExecutorService executorService;

    private void tryStart() {
        Log.d(TAG, "tryStart : ");
        videoDecoder.goOn();
        audioDecoder.goOn();
//        trackDemo();
        Log.d(TAG, "tryStart end : ");
    }

    private void pause() {
        videoDecoder.stop();
        audioDecoder.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MediaCodecList list = new MediaCodecList(MediaCodecList.REGULAR_CODECS);//REGULAR_CODECS参考api说明
        MediaCodecInfo[] codecs = list.getCodecInfos();
        Log.d(TAG, "Decoders: ");
        for (MediaCodecInfo codec : codecs) {
            if (codec.isEncoder())
                continue;
            Log.d(TAG, codec.getName());
        }
        Log.d(TAG, "Encoders: ");
        for (MediaCodecInfo codec : codecs) {
            if (codec.isEncoder())
                Log.d(TAG, codec.getName());
        }

        mStopButton = (Button) findViewById(R.id.buttonPauseRecording);
        mStartButton = (Button) findViewById(R.id.buttonStartRecording);
        mFlacButton = (Button) findViewById(R.id.buttonPauseFlac);
        mStopButton.setOnClickListener(mOnClickListener);
        mStartButton.setOnClickListener(mOnClickListener);
        mFlacButton.setOnClickListener(mOnClickListener);
        mWavButton = (Button) findViewById(R.id.buttonEncodeWav);
        mWavButton.setOnClickListener(mOnClickListener);
        mPcmButton = (Button) findViewById(R.id.buttonPausePcm);
        mPcmButton.setOnClickListener(mOnClickListener);
        mSurfaceView = findViewById(R.id.sfv);
        initPlayer();
    }

    private void initPlayer() {
        executorService =
                Executors.newFixedThreadPool(2);
        String videoFilePath = AudioFileFunc.getVideoFilePath();
        //  创建视频解码器
        videoDecoder = new VideoDecoder(videoFilePath, mSurfaceView, null);
        //  创建视频解码器
        executorService.execute(videoDecoder);
        audioDecoder = new AudioDecoder(videoFilePath);
        executorService.execute(audioDecoder);
    }

}
