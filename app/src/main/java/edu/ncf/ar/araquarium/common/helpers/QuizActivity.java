package edu.ncf.ar.araquarium.common.helpers;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.ar.core.AugmentedImage;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.HashMap;
import java.util.Map;

import edu.ncf.ar.araquarium.AugmentedImageActivity;
import edu.ncf.ar.araquarium.AugmentedImageNode;
import edu.ncf.ar.araquarium.QuizFragment;
import edu.ncf.ar.araquarium.R;
import edu.ncf.ar.araquarium.ResultFragment;

public class QuizActivity extends AppCompatActivity {

    private int questionId;
    private Resources mRes;
    private String currentFragment;

    // Augmented image and its associated center pose anchor, keyed by the augmented image in
    // the database.
    private final Map<AugmentedImage, AugmentedImageNode> augmentedImageMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRes = getResources();
        Intent intent = getIntent();
        if(intent != null){
            questionId = intent.getIntExtra(mRes.getString(R.string.qid), R.array.dummy_quiz);
        } else { questionId = R.array.dummy_quiz;}
        startQuiz(R.array.dummy_quiz);
    }

    public void startQuiz(int questionId){
        Log.d("activityMain", "Starting Quiz");
        currentFragment = mRes.getString(R.string.QUIZ);
        QuizFragment qf = new QuizFragment();
        Bundle quizArgs = new Bundle();
        quizArgs.putInt(mRes.getString(R.string.qid), questionId);
        qf.setArguments(quizArgs);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, qf, mRes.getString(R.string.QUIZ));
        fragmentTransaction.commit();
    }

    public void startResult(Boolean correct, int questionId){
        Log.d("activityMain", "Starting Result");
        currentFragment = mRes.getString(R.string.RESULT);
        ResultFragment rf = new ResultFragment();
        Bundle resultArgs = new Bundle();
        resultArgs.putInt(mRes.getString(R.string.qid), questionId);
        resultArgs.putBoolean(mRes.getString(R.string.bid), correct);
        rf.setArguments(resultArgs);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, rf, mRes.getString(R.string.QUIZ));
        fragmentTransaction.commit();
    }

    public void startAugmentedAquarium(){}

    public void startAugmentedImage(){
        Intent augImg = new Intent(this, AugmentedImageActivity.class);
        startActivity(augImg);
    }



}
