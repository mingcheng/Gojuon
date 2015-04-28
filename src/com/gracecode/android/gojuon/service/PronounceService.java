package com.gracecode.android.gojuon.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.util.LruCache;
import com.gracecode.android.gojuon.Characters;

import java.io.IOException;
import java.util.Arrays;


public class PronounceService extends Service {
    public static final String PLAY_PRONOUNCE_NAME = PronounceService.class.getName();
    public static final String EXTRA_CHARSET = "extra_charset";
    private static final int MAX_STREAMS = 5;

    private SoundPool mSoundPool;
    private AudioManager mAudioManager;
    private LruCache<String, String[]> mLruCache;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String charset = intent.getStringExtra(EXTRA_CHARSET);
            if (charset != null && charset.length() > 0) {
                try {
                    AssetFileDescriptor pronounceFile = getPronounceAssetFile(charset);
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
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String getRoumajiFromCharset(String needle) {
            String[] cached = mLruCache.get(needle);
            if (cached != null && cached.length > 0) {
                return cached[Characters.INDEX_ROUMAJI];
            }

            String[][][] all = new String[][][]{
                    Characters.MONOGRAPHS, Characters.DIGRAPHS, Characters.MONOGRAPHS_WITH_DIACRITICS, Characters.DIGRAPHS_WITH_DIACRITICS
            };

            for (String[][] characters : all) {
                for (String[] item : characters) {
                    if (Arrays.asList(item).contains(needle)) {
                        mLruCache.put(needle, item);
                        return item[Characters.INDEX_ROUMAJI];
                    }
                }
            }

            return "";
        }

        private AssetFileDescriptor getPronounceAssetFile(String charset) throws IOException {
            String roumaji = getRoumajiFromCharset(charset);
            roumaji = roumaji.replaceAll("(\\*+)$", "");
            switch (roumaji) {
                case "o/wo":
                    roumaji = "wo";
                    break;
            }
            return getAssets().openFd("pronounce/" + roumaji + ".ogg");
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder builder = new SoundPool.Builder();
            AudioAttributes.Builder attributes = new AudioAttributes.Builder();
            attributes.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY);

            builder.setAudioAttributes(attributes.build())
                    .setMaxStreams(MAX_STREAMS);
            mSoundPool = builder.build();
        } else {
            mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mLruCache = new LruCache<String, String[]>(Characters.MONOGRAPHS.length);

        registerReceiver(mBroadcastReceiver, new IntentFilter(PronounceService.PLAY_PRONOUNCE_NAME));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSoundPool.release();
        unregisterReceiver(mBroadcastReceiver);
    }
}
