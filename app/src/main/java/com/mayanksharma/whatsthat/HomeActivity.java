package com.mayanksharma.whatsthat;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mayanksharma.whatsthat.model.Course;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private Button buttonScan;
    private IntentIntegrator qrScan;
    String post_qrValue;
    String post_course;
    String post_year;
    String post_sem;
    String post_image;
    String id;
    private int flag = 0;
    private FirebaseDatabase mDatabase;
    private StorageReference mStorage;
    Data uid;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (shouldAskPermissions()) {
            askPermissions();
        }
        mDatabase = FirebaseDatabase.getInstance();


        buttonScan = (Button) findViewById(R.id.buttonScan);

        qrScan = new IntentIntegrator(this);

        //for scanning
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();

            }
        });

        //floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });




    }





    //scanning process
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            final String scanContent = scanningResult.getContents();

            if(scanContent == null)
            {
                Toast.makeText(HomeActivity.this, "No result", Toast.LENGTH_LONG).show();
            } else {//Toast.makeText(HomeActivity.this, scanContent, Toast.LENGTH_LONG).show();

                mDatabase.getReference("Docs").getRef().child(scanContent).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            Course course = dataSnapshot.getValue(Course.class);
                            ArrayList<Course> courseArrayList = new ArrayList<Course>();
                            courseArrayList.add(course);
                            Intent intent1 = new Intent(HomeActivity.this, FirstActivity.class);
                            intent1.putParcelableArrayListExtra("course", courseArrayList);
                            startActivity(intent1);
                            finish();

                        }else{
                            Toast.makeText(HomeActivity.this, "Result Not Found", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Home Scanner Error", "Failed to read app title value.", databaseError.toException());
                    }
                });
            }
          /*  if(scanContent == null)
            {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            }
            else if (scanContent.equals(id)) {
                //Toast toast = Toast.makeText(getApplicationContext(), "1234", Toast.LENGTH_SHORT);
                //toast.show();
            } else {
                Toast.makeText(HomeActivity.this, "Sorry something went wrong", Toast.LENGTH_LONG).show();
            }*/

        }
        else{
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }



}