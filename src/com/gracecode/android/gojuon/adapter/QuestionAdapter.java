package com.gracecode.android.gojuon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.common.Gojuon;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 15/4/16
 */
public class QuestionAdapter extends BaseCharactersAdapter<String> {
    public static final int MAX_SELECTION_COUNT = 4;
    private List<String> mQuestions;

    public QuestionAdapter(Context context, List<String> list) {
        super(context, list);
        setQuestion(list);
    }

    public void setQuestion(List<String> list) {
        setItem(list);
        notifyDataSetChanged();
        mQuestions = list;
    }

    public List<String> geAllItems() {
        return mQuestions;
    }

    public static final class Holder {
        @InjectView(R.id.character)
        TextView mCharacter;

        private Holder(View view) {
            ButterKnife.inject(this, view);
            view.setTag(this);

            if (Gojuon.useHandwritingFonts()) {
                mCharacter.setTypeface(Gojuon.getInstance().getCustomTypeface());
            }
        }

        public static Holder get(View view) {
            if (view.getTag() instanceof Holder) {
                return (Holder) view.getTag();
            }

            return new Holder(view);
        }

        public void setCharacter(String character) {
            this.mCharacter.setText(character);
        }
    }

    @Override
    public int getCount() {
        return MAX_SELECTION_COUNT;
    }

    @Override
    public String getItem(int i) {
        try {
            return super.getItem(i);
        } catch (RuntimeException e) {
            return "";
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_question, null);
        }

        Holder holder = Holder.get(view);
        holder.setCharacter(getItem(i));
        return view;
    }
}
