package com.mayanksharma.whatsthat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class FinishActivity extends AppCompatActivity {

    private Button log_out;
    private Button upl_agn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        log_out = (Button)findViewById(R.id.logout);
        upl_agn = (Button)findViewById(R.id.upload_again);

        upl_agn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(FinishActivity.this, UploadActivity.class);
                startActivity(intent1);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishActivity.this, HomeActivity.class);
                startActivity(intent);
                Toast.makeText(FinishActivity.this, "Logged Out", Toast.LENGTH_LONG).show();
            }
        });

    }

}
