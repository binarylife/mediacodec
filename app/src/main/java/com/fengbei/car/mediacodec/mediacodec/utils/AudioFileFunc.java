package com.fengbei.car.mediacodec.mediacodec.utils;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioFileFunc {
    //音频输入-麦克风
    public final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;

    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public final static int AUDIO_SAMPLE_RATE = 48000;  //44.1KHz,普遍使用的频率
    //录音输出文件
    private final static String AUDIO_RAW_FILENAME = "RawAudio.pcm";
    private final static String AUDIO_RAW_FINAL_FILENAME = "RawAudio.flac";
    private final static String AUDIO_WAV_FILENAME = "FinalAudio.wav";
    public final static String AUDIO_AMR_FILENAME = "FinalAudio.amr";
    private final static String AUDIO_FILE_BASE_PATH = "ChjFlac" + "/";
    private final static String AUDIO_FLAC_UPLOAD_FILE_BASE_PATH = "audioFlacDataFile" + "/";
    private final static String AUDIO_FLAC_UPLOAD_FILE_BASE_SYSTEM_PATH = "video_data" + "/";
    private final static String AUDIO_PCM_FILE_BASE_PATH = "VoicePCM" + "/";
    private final static String AUDIO_PCM_CONFIG_FILE_BASE_PATH = "hrsc" + "/" + "data.debug";
    public final static int SUCCESS = 1000;
    public final static int E_NOSDCARD = 1001;
    public final static int E_STATE_RECODING = 1002;
    public final static int E_UNKOWN = 1003;
    private final static String VIDEO_RAW_FILENAME = "normal_video.mp4";

    /**
     * 判断是否有外部存储设备sdcard
     *
     * @return true | false
     */
    public static boolean isSdcardExit() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取麦克风输入的原始音频流文件路径
     *
     * @return
     */
    public static String getRawFilePath() {
        String mAudioRawPath = "";
        if (isSdcardExit()) {
            mAudioRawPath = Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_RAW_FILENAME;
        }

        return mAudioRawPath;
    }


    /**
     * 获取麦克风输入的原始音频流文件路径
     *
     * @return
     */
    public static String getConfigFilePath() {
        String mAudioRawPath = "";
        if (isSdcardExit()) {
            mAudioRawPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AUDIO_PCM_CONFIG_FILE_BASE_PATH;
        }

        return mAudioRawPath;
    }

    /**
     * 获取编码后的WAV格式音频文件路径
     *
     * @return
     */
    public static String getWavFilePath() {
        String mAudioWavPath = "";
        if (isSdcardExit()) {
            mAudioWavPath = getFileBasePath() + AUDIO_WAV_FILENAME;
        }
        return mAudioWavPath;
    }


    /**
     * 获取原始视频频文件路径
     *
     * @return
     */
    public static String getVideoFilePath() {
        String mVdieoPath = "";
        if (isSdcardExit()) {
            mVdieoPath = getFileBasePath() + VIDEO_RAW_FILENAME;
        }
        return mVdieoPath;
    }


    /**
     * 获取编码后的AMR格式音频文件路径
     *
     * @return
     */
    public static String getAMRFilePath() {
        String mAudioAMRPath = "";
        if (isSdcardExit()) {
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mAudioAMRPath = fileBasePath + "/" + AUDIO_AMR_FILENAME;
        }
        return mAudioAMRPath;
    }


    /**
     * 获取麦克风输入的原始音频流文件路径
     *
     * @return
     */
    public static String getRawFinalFilePath() {
        String mAudioRawPath = "";
        if (isSdcardExit()) {
            mAudioRawPath = getFileBasePath() + AUDIO_RAW_FINAL_FILENAME;
        }

        return mAudioRawPath;
    }

    /**
     * 获取文件大小
     *
     * @param path,文件的绝对路径
     * @return
     */
    public static long getFileSize(String path) {
        File mFile = new File(path);
        if (!mFile.exists()) {
            return -1;
        }
        return mFile.length();
    }


    /**
     * 获取麦克风输入的原始音频流文件路径
     *
     * @return
     */
    public static String getFileBasePath() {
        String mAudioPcmPath = "";
        if (isSdcardExit()) {
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AUDIO_FILE_BASE_PATH;
            File file = new File(fileBasePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            mAudioPcmPath = fileBasePath;
        }

        return mAudioPcmPath;
    }


    /**
     * 获取麦克风输入的原始音频流文件路径
     *
     * @return
     */
    public static String getVoicePcmFileBasePath() {
        String mAudioPcmPath = "";
        if (isSdcardExit()) {
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AUDIO_PCM_FILE_BASE_PATH;
            File file = new File(fileBasePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            mAudioPcmPath = fileBasePath;
        }

        return mAudioPcmPath;
    }


    /**
     * 获取存储上传原始音频流文件路径
     *
     * @return
     */
    public static String getFlacFileUploadBasePath() {
        String mAudioPcmPath = "";
        if (isSdcardExit()) {
            String fileBasePath = "/" + AUDIO_FLAC_UPLOAD_FILE_BASE_SYSTEM_PATH + AUDIO_FLAC_UPLOAD_FILE_BASE_PATH;
            File file = new File(fileBasePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            mAudioPcmPath = fileBasePath;
        }

        return mAudioPcmPath;
    }


    /**
     * PCM文件转WAV文件
     *
     * @param inPcmFilePath  输入PCM文件路径
     * @param outWavFilePath 输出WAV文件路径
     * @param sampleRate     采样率，例如44100
     * @param channels       声道数 单声道：1或双声道：2
     * @param bitNum         采样位数，8或16
     */
    public static void convertPcm2Wav(String inPcmFilePath, String outWavFilePath, int sampleRate,
                                      int channels, int bitNum) {

        FileInputStream in = null;
        FileOutputStream out = null;
        byte[] data = new byte[1024];

        try {
            //采样字节byte率
            long byteRate = sampleRate * channels * bitNum / 8;

            in = new FileInputStream(inPcmFilePath);
            out = new FileOutputStream(outWavFilePath);

            //PCM文件大小
            long totalAudioLen = in.getChannel().size();

            //总大小，由于不包括RIFF和WAV，所以是44 - 8 = 36，在加上PCM文件大小
            long totalDataLen = totalAudioLen + 36;

            writeWaveFileHeader(out, totalAudioLen, totalDataLen, sampleRate, channels, byteRate);

            int length = 0;
            while ((length = in.read(data)) > 0) {
                out.write(data, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 输出WAV文件
     *
     * @param out           WAV输出文件流
     * @param totalAudioLen 整个音频PCM数据大小
     * @param totalDataLen  整个数据大小
     * @param sampleRate    采样率
     * @param channels      声道数
     * @param byteRate      采样字节byte率
     * @throws IOException
     */
    private static void writeWaveFileHeader(FileOutputStream out, long totalAudioLen,
                                            long totalDataLen, int sampleRate, int channels, long byteRate) throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);//数据大小
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';//WAVE
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        //FMT Chunk
        header[12] = 'f'; // 'fmt '
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';//过渡字节
        //数据大小
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        //编码方式 10H为PCM编码格式
        header[20] = 1; // format = 1
        header[21] = 0;
        //通道数
        header[22] = (byte) channels;
        header[23] = 0;
        //采样率，每个通道的播放速度
        header[24] = (byte) (sampleRate & 0xff);
        header[25] = (byte) ((sampleRate >> 8) & 0xff);
        header[26] = (byte) ((sampleRate >> 16) & 0xff);
        header[27] = (byte) ((sampleRate >> 24) & 0xff);
        //音频数据传送速率,采样率*通道数*采样深度/8
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        // 确定系统一次要处理多少个这样字节的数据，确定缓冲区，通道数*采样位数
        header[32] = (byte) (channels * 16 / 8);
        header[33] = 0;
        //每个样本的数据位数
        header[34] = 16;
        header[35] = 0;
        //Data chunk
        header[36] = 'd';//data
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }

}
