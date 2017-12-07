package com.mayanksharma.whatsthat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText et_username, et_pass;
    private String username, pass;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = (EditText)findViewById(R.id.username);
        et_pass = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(); // called when the button is clicked to validate the input
            }
        });
    }

    public void register()
    {
        initialize(); // initialize the input to string variables
        if(!validate())
        {
            Toast.makeText(this,"Login Failed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            onSignUpSuccess();
        }
    }

    public void onSignUpSuccess()
    {
        // TODO what will go after the valid input
        if (username.equals("admin") && pass.equals("admin123"))
        {
            Intent intent = new Intent(LoginActivity.this, UploadActivity.class);
            startActivity(intent);
            Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_LONG).show();
        } else
        {
            Toast.makeText(LoginActivity.this, "Please try again", Toast.LENGTH_LONG).show();
        }

    }

    public boolean validate()
    {
        boolean valid = true;
        if(!username.equals("admin") || username.isEmpty() || username.length()>30)
        {
            et_username.setError("Please enter valid Username");
            valid = false;
        }
        if (!pass.equals("admin123") || pass.isEmpty())
        {
            et_pass.setError("Please enter valid Password");
        }
        return valid;
    }

    public void initialize()
    {
        username = et_username.getText().toString().trim();
        pass = et_pass.getText().toString().trim();
    }
}
