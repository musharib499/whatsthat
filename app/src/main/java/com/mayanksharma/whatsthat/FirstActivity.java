package com.mayanksharma.whatsthat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.mayanksharma.whatsthat.model.Course;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity {
    private Button back,pdfView;
    private TextView tv_course;
    private TextView tv_sem;
    private TextView tv_year;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
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


      /*  //adding a clicklistener on listview
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the upload
                Data data = dataList.get(i);

                //Opening the upload file in browser using the upload url
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(data.getUrl()));
                startActivity(intent);
            }
        });
*/
     /*   //retrieving upload data from firebase database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Data data = postSnapshot.getValue(Data.class);
                    dataList.add(data);
                }

                String[] uploads = new String[dataList.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = dataList.get(i).getUrl();
                }

                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    public void showData(ArrayList<Course> courses) {
           /* ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataSnapshot);
            mListView.setAdapter(adapter);*/
        final Course cour=courses.get(0);

        tv_sem.setText(cour.getSem());
        tv_year.setText(cour.getYear());
        tv_course.setText(cour.getCourse());
        download(cour.getUrl(),cour.getCourse());
        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view(cour.getCourse());
            }
        });

    }

    public void download(String url,String pdfName)
    {
        new DownloadFile().execute(url, ""+pdfName+".pdf");
    }

    public void view(String key)
    {
        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ ""+key+".pdf");  // -> filename = maven.pdf
       // Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + "course_pdf",key+".pdf" );
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(FirstActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressbar.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.GONE);
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File folder = new File(extStorageDirectory);
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressbar.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);
            super.onPostExecute(aVoid);
        }


    }




}
