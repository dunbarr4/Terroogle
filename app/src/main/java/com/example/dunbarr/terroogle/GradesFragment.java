package com.example.dunbarr.terroogle;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class GradesFragment extends Fragment {

    private TextView totalTV;
    private ListView assignLV;
    private List<Assignment> assignments;
    private ArrayAdapter<Assignment> adapter;
    public static final String TAG = "GradesFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grades, container, false);

        assignments = AssignmentLibrary.getInstance().getLibrary();


        //changed "this" to "getActivity()"
        adapter = new CustomAdapter();
        totalTV = view.findViewById(R.id.totalGradeOverall);
        assignLV = view.findViewById(R.id.assignLists);

        assignLV.setAdapter(adapter);


        String whereClause = "";
        DataQueryBuilder query = DataQueryBuilder.create();
        query.setWhereClause(whereClause);

        Backendless.Persistence.of(Assignment.class).find(query, new AsyncCallback<List<Assignment>>() {
            @Override
            public void handleResponse(List<Assignment> response) {
                assignments.clear();
                assignments.addAll(response);
                adapter.notifyDataSetChanged();
                setTotalScore();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d(TAG, fault.toString());
            }
        });

        registerForContextMenu(assignLV);

        return view;

    }

    private class CustomAdapter extends ArrayAdapter<Assignment> {

        public CustomAdapter(){
            super(GradesFragment.this.getActivity(), R.layout.assignment_custom_list_item,
                    AssignmentLibrary.getInstance().getLibrary());
        }

        public View getView(int position, View convertView, ViewGroup parent){
            //return null;

            if(convertView == null){
                convertView = LayoutInflater.from(GradesFragment.this.getActivity())
                        .inflate(R.layout.assignment_custom_list_item, parent, false);
            }

            Assignment currentTrip = AssignmentLibrary.getInstance().getLibrary().get(position);

            TextView name = convertView.findViewById(R.id.nameId);
            ImageView emojiIcon = convertView.findViewById(R.id.faceId);
            TextView points = convertView.findViewById(R.id.pointsId);
            TextView percent = convertView.findViewById(R.id.percentId);
            TextView letter = convertView.findViewById(R.id.letterId);
            TextView comment = convertView.findViewById(R.id.commentId);
            LinearLayout bgElement = convertView.findViewById(R.id.container);
            name.setText(currentTrip.getName());
            points.setText(currentTrip.getPointsE() + " / " + currentTrip.getPointsP());
            percent.setText("   " + currentTrip.getPercent() + "%");

            if (currentTrip.getPercent() >= 90) {
                emojiIcon.setImageResource(R.drawable.ic_smiley_face);
                letter.setText("   A   ");
                comment.setText("Absolutely Awesome!");
                bgElement.setBackgroundColor(Color.parseColor("#42f45f"));
            } else if (currentTrip.getPercent() >= 80) {
                emojiIcon.setImageResource(R.drawable.ic_smiley_b);
                letter.setText("   B   ");
                comment.setText("Barely missed the mark!");
                bgElement.setBackgroundColor(Color.parseColor("#bbf441"));
            } else if (currentTrip.getPercent() >= 70) {
                emojiIcon.setImageResource(R.drawable.ic_smiley_c);
                letter.setText("   C   ");
                comment.setText("C you in summer school!");
                bgElement.setBackgroundColor(Color.parseColor("#f4e541"));
            } else if (currentTrip.getPercent() >= 60) {
                emojiIcon.setImageResource(R.drawable.ic_smiley_d);
                letter.setText("   D   ");
                comment.setText("Try studying?");
                bgElement.setBackgroundColor(Color.parseColor("#f47c41"));
            } else {
                emojiIcon.setImageResource(R.drawable.ic_smiley_f);
                letter.setText("   F   ");
                comment.setText("You lost.");
                bgElement.setBackgroundColor(Color.parseColor("#FF0000"));
            }

            return convertView;

        }

        public int count(){
            return AssignmentLibrary.getInstance().getLibrary().size();
        }

    }

    private int getTotalPossible() {

        return 5 * assignments.size();
    }

    private int getTotalEarned() {
        int amount = 0;
        for (Assignment a : assignments) {
            amount += a.getPointsE();
        }
        return amount;
    }

    private int getTotalPercent() {
        if(assignments.size() == 0){
            return 0;
        }

        return (getTotalEarned() * 100 / getTotalPossible());
    }

    private String getLetterGrade() {
        if (getTotalPercent() >= 90) {
            return "A";
        } else if (getTotalPercent() >= 80) {
            return "B";
        } else if (getTotalPercent() >= 70) {
            return "C";
        } else if (getTotalPercent() >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

    private void setTotalScore(){
        totalTV.setText(String.format("Total Grade: %d/%d - %d%% - %s", getTotalEarned(), getTotalPossible(),
                getTotalPercent(), getLetterGrade()));
    }

}
