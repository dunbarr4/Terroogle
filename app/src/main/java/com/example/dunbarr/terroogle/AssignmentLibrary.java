package com.example.dunbarr.terroogle;

import java.util.ArrayList;
import java.util.List;

public class AssignmentLibrary {

    private static final AssignmentLibrary ourInstance = new AssignmentLibrary();

    private List<Assignment> library;

    public static AssignmentLibrary getInstance() {
        return ourInstance;
    }

    private AssignmentLibrary() {
        library = new ArrayList();
    }

    public List<Assignment> getLibrary() {
        return library;
    }

}
