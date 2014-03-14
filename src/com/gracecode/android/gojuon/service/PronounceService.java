package com.gracecode.android.gojuon.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;


public class PronounceService extends Service {
    public static final String PLAY_PRONOUNCE_NAME = PronounceService.class.getName();
    public static final String EXTRA_ROUMAJI = "extra_roumaji";

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String roumaji = intent.getStringExtra(EXTRA_ROUMAJI);
            if (roumaji != null && roumaji.length() > 0) {
                try {
                    AssetFileDescriptor pronounceFile = getPronounceAssetFile(roumaji);
                    if (pronounceFile.getLength() > 0) {
                        final int playId = mSoundPool.load(pronounceFile, 1);
                        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                            @Override
                            public void onLoadComplete(SoundPool soundPool, int i, int i2) {
                                int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                mSoundPool.play(playId, volume, volume, 1, 0, 1f);
                                mSoundPool.unload(playId);
                            }
                        });
                    } else {
                        Toast.makeText(PronounceService.this, pronounceFile.toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private AssetFileDescriptor getPronounceAssetFile(String roumaji) throws IOException {
            roumaji = roumaji.replaceAll("(\\*+)$", "") ;
            switch (roumaji) {
                case "o/wo":
                    roumaji = "wo";
                    break;
            }
            return getAssets().openFd("pronounce/" + roumaji + ".ogg");
        }
    };
    private SoundPool mSoundPool;
    private AudioManager mAudioManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(mBroadcastReceiver, new IntentFilter(PronounceService.PLAY_PRONOUNCE_NAME));
        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        mSoundPool.release();
    }
}
