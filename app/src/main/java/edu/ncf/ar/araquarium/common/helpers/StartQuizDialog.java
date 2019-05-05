package edu.ncf.ar.araquarium.common.helpers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import edu.ncf.ar.araquarium.R;

public class StartQuizDialog extends DialogFragment {
    private int questionID;
    private String questionName;
    private StartQuizDialogListener listener;

    public interface StartQuizDialogListener{
        public void startQuiz(int quizId);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (getArguments() != null) {
            questionID = getArguments().getInt(getResources().getString(R.string.qid));
            questionName = getArguments().getString(getResources().getString(R.string.qname));
        }
        String message = getResources().getString(R.string.quizDialog)+" "+questionName+" ?";
        builder.setMessage(message)
                .setPositiveButton(R.string.startQuiz, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.startQuiz(questionID);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StartQuizDialog.this.getDialog().cancel();;
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{listener = (StartQuizDialogListener) context;}
        catch (ClassCastException e){
            Log.d("StartQuizDialog", e.toString());
        }

    }
}
