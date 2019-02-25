package com.example.dunbarr.terroogle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //public static final String APPLICATION_ID = "D2E5F9E6-9223-39C2-FF93-DE7AC2162300";
    //public static final String SECRET_KEY = "528276A7-1BAA-C39F-FF49-839811194800";
    public static final String TAG = "MainActivity";
    public static AssignmentName assignmentName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(assignmentName == null) {
            Backendless.Persistence.of(AssignmentName.class).find(new AsyncCallback<List<AssignmentName>>() {
                @Override
                public void handleResponse(List<AssignmentName> response) {
                    if(response.size() > 0) {
                        assignmentName = response.get(0);
                    }else{
                        assignmentName = new AssignmentName(11);
                    }

                    Log.d("MainActivity", assignmentName.toString());

                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Log.d("MainActivity", fault.toString());
                    assignmentName = new AssignmentName(11);
                    Log.d("MainActivity", assignmentName.toString());
                    Backendless.Persistence.save(assignmentName, new AsyncCallback<AssignmentName>() {
                        @Override
                        public void handleResponse(AssignmentName response) {
                            Log.d("MainActivity", response.toString());

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.d("MainActivity", fault.toString());
                        }
                    });
                }
            });

        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new GradesFragment());
        ft.commit();

    }

//    private String getLetterGrade(double getTotalPercent) {
//        if (getTotalPercent() >= 90) {
//            return "A";
//        } else if (getTotalPercent() >= 80) {
//            return "B";
//        } else if (getTotalPercent() >= 70) {
//            return "C";
//        } else if (getTotalPercent() >= 60) {
//            return "D";
//        } else {
//            return "F";
//        }
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.menu_log_out){
            Backendless.UserService.logout(new AsyncCallback<Void>() {
                @Override
                public void handleResponse(Void response) {
                    Toast.makeText(MainActivity.this, "You have successfully logged out.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Log.e("TripListActivity", fault.toString());
                }
            });


        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.math_fragment) {
            Log.d("MainActivity", "Math Selected");
            fragment = new MathFragment();
        } else if (id == R.id.grades) {
            Log.d("MainActivity", "Grades Selected");
            fragment = new GradesFragment();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(assignmentName != null) {
            Backendless.Persistence.save(assignmentName, new AsyncCallback<AssignmentName>() {
                @Override
                public void handleResponse(AssignmentName response) {
                    Log.d("MainActivity", response.toString());
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });
        }
    }
}
