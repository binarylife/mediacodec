package com.fengbei.car.mediacodec;

import android.app.Activity;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

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
    }
}
