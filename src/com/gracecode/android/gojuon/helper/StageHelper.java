package com.gracecode.android.gojuon.helper;

import android.content.Context;
import android.content.res.Resources;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.dao.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 15/4/17
 */
final public class StageHelper {
    private static StageHelper mInstance;
    private final Context mContext;
    private static List<Stage> mStages = new ArrayList<>();

    public static StageHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new StageHelper(context);
        }
        return mInstance;
    }

    StageHelper(Context context) {
        mContext = context;
        init();
    }

    public Stage getStage(int level) {
        return mStages.get(level);
    }

    private void init() {
        mStages.clear();
        readStageFromRaw();
    }

    private String readRawTextFile(Context ctx, int resId) throws IOException {
        Resources resources = mContext.getResources();
        InputStream inputStream = resources.openRawResource(resId);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        return new String(bytes);
    }

    private void readStageFromRaw() {
        try {
            String json = readRawTextFile(mContext, R.raw.stage);
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0, size = jsonArray.length(); i < size; i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String[] syllabus = getSyllabusFromJSONArray(item.getJSONArray("syllabus"));
                mStages.add(i, new Stage(i + 1, syllabus, item.getInt("timeout")));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getSyllabusFromJSONArray(JSONArray jsonArray) throws JSONException {
        String[] syllabus = new String[jsonArray.length()];
        for (int i = 0, length = jsonArray.length(); i < length; i++) {
            syllabus[i] = jsonArray.getString(i);
        }
        return syllabus;
    }

    public void setStage(int level, String[] data, int timeout) {
        Stage stage = new Stage(level, data, timeout);
        mStages.add(level, stage);
    }

    public void setStage(int level, String[] data) {
        Stage stage = new Stage(level, data);
        mStages.add(level, stage);
    }

    public int getScore(int level) {
        try {
            return getStage(level).getScore();
        } catch (RuntimeException e) {
            return Stage.NONE_SCORE;
        }
    }

    public List<Stage> getAllStages() {
        return mStages;
    }
}
