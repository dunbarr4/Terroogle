package com.example.dunbarr.terroogle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        assignments = new ArrayList<>();


        //changed "this" to "getActivity()"
        adapter = new ArrayAdapter(getActivity(), R.layout.list_item_assignment, assignments);
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
