package edu.ncf.ar.araquarium;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizFragment extends Fragment {

    private Resources mRes;
    private View rootView;
    private Button btnQuit, btnSubmit;
    private RadioGroup radioGroup;
    private RadioButton radioBtn1, radioBtn2,radioBtn3, radioBtn4;
    private TextView questionText;
    private int correctAnswerId;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRes = getActivity().getResources();
        rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
        btnQuit = rootView.findViewById(R.id.quizExitButton);
        btnSubmit = rootView.findViewById(R.id.quizSubmitButton);
        questionText = rootView.findViewById(R.id.quizQuestionText);
        radioGroup = rootView.findViewById(R.id.quizRadioGroup);
        radioBtn1 = rootView.findViewById(R.id.radioButton);
        radioBtn2 = rootView.findViewById(R.id.radioButton2);
        radioBtn3 = rootView.findViewById(R.id.radioButton3);
        radioBtn4 = rootView.findViewById(R.id.radioButton4);
        if(getArguments()!=null){

            int questionId = getArguments().getInt(mRes.getString(R.string.qid));
            String[] question = mRes.getStringArray(questionId);
            //set question text
            questionText.setText(question[0]);
            //set answers
            radioBtn1.setText(question[1]);
            radioBtn2.setText(question[2]);
            radioBtn3.setText(question[3]);
            radioBtn4.setText(question[4]);
            //correct answer
            correctAnswerId = Integer.parseInt(question[5]);
            //set answer images
            radioBtn1.setCompoundDrawablesWithIntrinsicBounds(getContext().getDrawable(mRes.getIdentifier(question[6],
                    "drawable", getActivity().getPackageName())), null, null, null);
            radioBtn2.setCompoundDrawablesWithIntrinsicBounds(getContext().getDrawable(mRes.getIdentifier(question[7],
                    "drawable", getActivity().getPackageName())), null, null, null);
            radioBtn3.setCompoundDrawablesWithIntrinsicBounds(getContext().getDrawable(mRes.getIdentifier(question[8],
                    "drawable", getActivity().getPackageName())), null, null, null);
            radioBtn4.setCompoundDrawablesWithIntrinsicBounds(getContext().getDrawable(mRes.getIdentifier(question[9],
                    "drawable", getActivity().getPackageName())), null, null, null);
        }else{
            questionText.setText("Error! Error!");
            correctAnswerId=-1;
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPressed();
            }
        });

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitPressed();
            }
        });

        return rootView;
    }

    private void quitPressed(){
        //stubbed, return to home fragment
    }

    private void submitPressed(){
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButtonID);
        int idx = radioGroup.indexOfChild(radioButton);
        if(idx == correctAnswerId) {
            Toast.makeText(getActivity(),"Got it right!", Toast.LENGTH_LONG).show();
            submitResult(true);
        }else{
            Toast.makeText(getActivity(),"Got it wrong!", Toast.LENGTH_LONG).show();
            submitResult(false);
        }
    }

    public void submitResult(Boolean correct){
        //stubbed, goto result screen fragment with result
    }

}
