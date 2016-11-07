package com.example.heyong.exercisesbase.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.Bean.UserAttr;
import com.example.heyong.exercisesbase.R;

public class StudentViewPagerFragment extends Fragment implements View.OnClickListener {

    private TextView queTextView;
    private RadioGroup radioGroup;
    private Note note;
    private RelativeLayout checkRL;

    public static StudentViewPagerFragment getInstance(Note note) {
        Bundle bundle = new Bundle();
        bundle.putStringArray("note", new String[]{note.getQuestion(), note.getAnswer()});
        StudentViewPagerFragment testFm = new StudentViewPagerFragment();
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
        View view = inflater.inflate(R.layout.student_fragment, container, false);

        queTextView = (TextView) view.findViewById(R.id.que_text);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        checkRL = (RelativeLayout) view.findViewById(R.id.rl_check);

        checkRL = (RelativeLayout) view.findViewById(R.id.rl_check);
        queTextView.setText(note.getQuestion());
        checkRL.setOnClickListener(this);
        if (note.getAnswer().length() > 1) {
            //非选择题
            radioGroup.setVisibility(View.GONE);
            checkRL.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_check:
                boolean flag = checkByRadioId(radioGroup.getCheckedRadioButtonId());
                //Toast.makeText(getContext(), "" + flag, Toast.LENGTH_SHORT).show();
                resultDialog(flag);
                break;
            default:
                break;
        }
    }

    private boolean checkByRadioId(int id) {
        boolean flag = false;
        String ans = note.getAnswer();
        switch (id) {
            case R.id.radio_A:
                if (ans.equals("A"))
                    flag = true;
                break;
            case R.id.radio_B:
                if (ans.equals("B"))
                    flag = true;
                break;
            case R.id.radio_C:
                if (ans.equals("C"))
                    flag = true;
                break;
            case R.id.radio_D:
                if (ans.equals("D"))
                    flag = true;
                break;
            default:
                break;
        }
        return flag;
    }

    private void resultDialog(boolean flag) {
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        if (flag) {
            builder.setIcon(R.drawable.smile);
            builder.setMessage("恭喜你答对了"); //设置内容
        } else {
            builder.setIcon(R.drawable.confused);
            builder.setMessage("很遗憾，答错了，再看看吧");
        }
        builder.setPositiveButton("知道了", null);
        builder.create().show();
    }
}
