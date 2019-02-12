package com.example.dunbarr.terroogle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class MathFragment extends Fragment {

    public static final String APPLICATION_ID = "E967F40B-DEBD-A30F-FF35-0B5EF1716700";
    public static final String SECRET_KEY = "AC836F9B-FB5C-F92A-FF87-750A6C141000";
    public static final String TAG = "MainActivity";
    private TextView question, error, assignedName;
    private EditText answer;
    private Button submit;
    private int qAnswer, correct, possible, high, firstPart, secondPart, attempts, assignNum;
    String currentName;

    private Assignment currentAssignment;
    private List<Assignment> Assignments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_math, container, false);

        question = view.findViewById(R.id.questionTview);
        error = view.findViewById(R.id.errorMessage);
        answer = view.findViewById(R.id.userAnswerBox);
        submit = view.findViewById(R.id.answerButton); 
        assignedName = view.findViewById(R.id.assignedName);
        Assignments = new ArrayList<>();
        possible = 5;
        high = 10;
        correct = 0;
        attempts = 0;



        hideErrorText("");

        //if(nameTaken()){
            //createMathName();
        //}

        createQuestion();
        createMathName();

        //need to create random question here, that includes an answer;
        //random question/answer could work like,  (random number) * (random number) = (answer)

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = assignedName.getText().toString();
                int sAnswer = Integer.parseInt(answer.getText().toString());

                if((answer.getText().toString()).isEmpty()){
                    showErrorText("You must enter a number before submitting");
                }else if(sAnswer == qAnswer){
                    correct++;
                    hideErrorText("");
                    createQuestion();
                    attempts++;
                }else{
                    attempts++;
                    createQuestion();
                    hideErrorText("");
                }

                answer.setText("");

                if(attempts == possible){
                    currentAssignment = new Assignment(currentName, correct);
                    Assignments.add(currentAssignment);

                    Backendless.Persistence.save(currentAssignment, new AsyncCallback<Assignment>() {
                        @Override
                        public void handleResponse(Assignment response) {
                            Log.d(TAG, response.getName() + " was saved");
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.d(TAG, fault.toString());
                        }
                    });

                    attempts = 0;
                    correct = 0;
//                    assignedName.setText("");
                    createMathName();
                    showErrorText("You have started a new assignment.");

                }

            }
        });

        return view;

    }


    public int getRandomInt(int high){
        Log.d("MainActivity", "Generating Number with max " + high);
        int range = high - 1;
        return (int)(Math.random() *  (range + 1) + 1);
    }

    private void createQuestion(){
        firstPart = getRandomInt(high);
        secondPart = getRandomInt(high);
        qAnswer = firstPart * secondPart;
        question.setText(String.format("%d * %d", firstPart, secondPart));
    }

    private void createMathName(){
        String chapter, part;

        Log.d("MathFragment", MainActivity.assignmentName.toString());

        if(MainActivity.assignmentName != null) {
            assignNum = MainActivity.assignmentName.getNumber();
            MainActivity.assignmentName.setNumber(MainActivity.assignmentName.getNumber() + 2);

            String assignString = "" + assignNum;

            if (assignString.length() > 3) {
                MainActivity.assignmentName.setNumber(11);
                chapter = assignString.substring(0, 1);
                part = assignString.substring(1);
            } else if (assignString.length() == 3) {
                chapter = assignString.substring(0, 2);
                part = assignString.substring(2);
            } else {
                chapter = assignString.substring(0, 1);
                part = assignString.substring(1);
            }

            currentName = "Math " + chapter + "." + part;
            assignedName.setText(currentName);
        }
    }


    private void showErrorText(String message) {

        error.setText(message);
        error.setVisibility(View.VISIBLE);
    }

    private void hideErrorText(String message) {
        error.setText(message);
        error.setVisibility(View.INVISIBLE);
    }

}
