package com.mayanksharma.whatsthat;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.mayanksharma.whatsthat.model.Course;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity {
    private Button back,pdfView;
    private TextView tv_course;
    private TextView tv_sem;
    private TextView tv_year;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private long enqueue;
    private DownloadManager dm;
    String id;
    Data uid;
    private String getting_course, getting_sem, getting_year, get_id;
    private LinearLayout progressbar;
    List<Data> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ArrayList<Course> course = this.getIntent().getParcelableArrayListExtra("course");

        dataList = new ArrayList<>();
        progressbar = (LinearLayout) findViewById(R.id.llprogressbar);
        tv_sem = (TextView)findViewById(R.id.post_sem);
        tv_course = (TextView)findViewById(R.id.post_course);
        tv_year = (TextView)findViewById(R.id.post_year);
        back = (Button)findViewById(R.id.back1);
        pdfView = (Button)findViewById(R.id.view);
        showData(course);

        //to go back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(this, "Thank You for using WHATS THIS?", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(FirstActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


    }

    public void showData(ArrayList<Course> courses) {
        final Course cour=courses.get(0);

        tv_sem.setText(cour.getSem());
        tv_year.setText(cour.getYear());
        tv_course.setText(cour.getCourse());
        download(cour.getUrl(),cour.getCourse());
       /* progressbar.setVisibility(View.GONE);
        pdfView.setVisibility(View.VISIBLE);
        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(cour.getUrl()+".pdf"));
                intent.putExtra("url",cour.getUrl());
                startActivity(intent);
            }
        });*/



    }

    public void download(String url,String pdfName)
    {
        progressbar.setVisibility(View.VISIBLE);
        pdfView.setVisibility(View.GONE);
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+pdfName+".pdf");
        enqueue = dm.enqueue(request);
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            progressbar.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);

                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(enqueue);
                Cursor c = dm.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c
                            .getInt(columnIndex)) {

                        //  ImageView view = (ImageView) findViewById(R.id.imageView1);
                        final String uriString = c
                                .getString(c
                                        .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        pdfView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(FirstActivity.this,WevViewPdf.class);
                                intent.putExtra("url",uriString);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        }
    };






    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }



}
