package com.example.heyong.exercisesbase.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.R;


public class ViewPagerFragment extends Fragment implements View.OnClickListener {
    private TextView que_text;
    private TextView ans_text;
    private Note note;


    public static ViewPagerFragment getInstance(Note note) {
        Bundle bundle = new Bundle();
        bundle.putStringArray("note", new String[]{note.getQuestion(), note.getAnswer()});
        ViewPagerFragment testFm = new ViewPagerFragment();
        testFm.setArguments(bundle);
        return testFm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String[] s = bundle.getStringArray("note");
            this.note = new Note(s[0], s[1]);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_fragment, container, false);
        que_text = (TextView) view.findViewById(R.id.que_text);
        ans_text = (TextView)view.findViewById(R.id.answer_text);
        que_text.setText(note.getQuestion());
        ans_text.setText("答案："+note.getAnswer());
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View v) {

    }
}
