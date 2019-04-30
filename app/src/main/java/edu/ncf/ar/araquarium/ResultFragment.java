package edu.ncf.ar.araquarium;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultFragment extends Fragment {

    private Resources mRes;
    private View rootView;
    private Button btnBackpack, btnCamera, btnTryAgain;
    private TextView tvCorrect, tvExplanation;
    private ImageView iv;
    private int questionId;
    private String modelURI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRes = getActivity().getResources();
        rootView = inflater.inflate(R.layout.fragment_result, container, false);
        btnBackpack = rootView.findViewById(R.id.btnBackpack);
        btnCamera = rootView.findViewById(R.id.btnCamera);
        btnTryAgain = rootView.findViewById(R.id.btnTryAgain);
        tvCorrect = rootView.findViewById(R.id.tvCorrect);
        tvExplanation = rootView.findViewById(R.id.tvExplanation);
        iv = rootView.findViewById(R.id.ivModelImage);
        btnBackpack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backpackPressed();
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPressed();
            }
        });

        if(getArguments()!=null) {
            questionId = getArguments().getInt(mRes.getString(R.string.qid));
            String[] question = mRes.getStringArray(questionId);
            modelURI = question[10];
            tvExplanation.setText(question[12]);
            Boolean isCorrect = getArguments().getBoolean(mRes.getString(R.string.bid));
            if(isCorrect){
                btnTryAgain.setVisibility(View.INVISIBLE);
                tvCorrect.setText(mRes.getString(R.string.correct));
                iv.setImageDrawable(getContext().getDrawable(mRes.getIdentifier(question[11],
                        "drawable", getActivity().getPackageName())));
                //Add code to unlock model
            }else{
                tvCorrect.setText(mRes.getString(R.string.incorrect));
                btnTryAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tryAgainPressed();
                    }
                });
            }
        }
        return rootView;
    }

    public void tryAgainPressed(){
        MainActivity ma = (MainActivity) getActivity();
        ma.startQuiz(questionId);
    }

    public void backpackPressed(){
        MainActivity ma = (MainActivity) getActivity();
        ma.startAugmentedAquarium();
    }

    public void cameraPressed(){
        MainActivity ma = (MainActivity) getActivity();
        ma.startAugmentedImage();
    }

}
