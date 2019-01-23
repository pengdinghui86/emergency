package com.dssm.esc.util;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.Time;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class VoiceRecorderUtil {
    MediaRecorder recorder;
    static final String PREFIX = "voice";
    static final String EXTENSION = ".amr";
    private boolean isRecording = false;
    private long startTime;
    private String voiceFilePath = null;
    private String voiceFileName = null;
    private File file;
    private Handler handler;

    public VoiceRecorderUtil(Handler var1) {
        this.handler = var1;
    }

    public String startRecording(String var1, String var2, Context var3) {
        this.file = null;

        try {
            if(this.recorder != null) {
                this.recorder.release();
                this.recorder = null;
            }

            this.recorder = new MediaRecorder();
            this.recorder.setAudioSource(1);
            this.recorder.setOutputFormat(3);
            this.recorder.setAudioEncoder(1);
            this.recorder.setAudioChannels(1);
            this.recorder.setAudioSamplingRate(8000);
            this.recorder.setAudioEncodingBitRate(64);
            this.voiceFileName = this.getVoiceFileName(var2);
            this.voiceFilePath = this.getVoiceFilePath();
            this.file = new File(this.voiceFilePath);
            this.recorder.setOutputFile(this.file.getAbsolutePath());
            this.recorder.prepare();
            this.isRecording = true;
            this.recorder.start();
        } catch (IOException var5) {

        }

        (new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        if(VoiceRecorderUtil.this.isRecording) {
                            Message var1 = new Message();
                            var1.what = VoiceRecorderUtil.this.recorder.getMaxAmplitude() * 13 / 32767;
                            VoiceRecorderUtil.this.handler.sendMessage(var1);
                            SystemClock.sleep(100L);
                            continue;
                        }
                    } catch (Exception var2) {

                    }

                    return;
                }
            }
        })).start();
        this.startTime = (new Date()).getTime();
        return this.file == null?null:this.file.getAbsolutePath();
    }

    public void discardRecording() {
        if(this.recorder != null) {
            try {
                this.recorder.stop();
                this.recorder.release();
                this.recorder = null;
                if(this.file != null && this.file.exists() && !this.file.isDirectory()) {
                    this.file.delete();
                }
            } catch (IllegalStateException var2) {
                ;
            } catch (RuntimeException var3) {
                ;
            }

            this.isRecording = false;
        }

    }

    public int stopRecoding() {
        if(this.recorder != null) {
            this.isRecording = false;
            this.recorder.stop();
            this.recorder.release();
            this.recorder = null;
            if(this.file != null && this.file.exists() && this.file.isFile()) {
                if(this.file.length() == 0L) {
                    this.file.delete();
                    return -1011;
                } else {
                    int var1 = (int)((new Date()).getTime() - this.startTime) / 1000;
                    return var1;
                }
            } else {
                return -1011;
            }
        } else {
            return 0;
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if(this.recorder != null) {
            this.recorder.release();
        }

    }

    public String getVoiceFileName(String var1) {
        Time var2 = new Time();
        var2.setToNow();
        return var1 + var2.toString().substring(0, 15) + ".amr";
    }

    public boolean isRecording() {
        return this.isRecording;
    }

    public String getVoiceFilePath() {
        File voiceFile = PathUtil.getInstance().getVoicePath();
        if(!voiceFile.exists())
            voiceFile.mkdirs();
        String voiceFilePath = PathUtil.getInstance().getVoicePath().getAbsolutePath();
        return voiceFilePath + "/" + this.voiceFileName;
    }
}

