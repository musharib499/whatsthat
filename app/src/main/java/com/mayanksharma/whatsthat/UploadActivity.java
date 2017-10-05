package com.mayanksharma.whatsthat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class UploadActivity extends AppCompatActivity {
    private Spinner spinner1, spinner2, spinner3;
    private ArrayAdapter<CharSequence> adapter1, adapter2, adapter3;
    private Button Upload_btn, Pdf_btn, Qr_generate;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private static final int GALLERY_REQUEST = 1;
    private ImageView Image;
    private TextView PdfStatus;
    private String text2Qr;
    String get_id;
    private Uri mQrCodeUri = null;
    //ProgressBar progressBar;
    private ProgressDialog mProgress;
    private Uri mPdfUri = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorage = storage.getReferenceFromUrl("gs://whatsthat-52cbb.appspot.com/");
        mDatabase = FirebaseDatabase.getInstance().getReference("Docs");

        spinner1 = (Spinner)findViewById(R.id.course_spinner);
        spinner2 = (Spinner)findViewById(R.id.year_spinner);
        spinner3 = (Spinner)findViewById(R.id.semester_spinner);

        adapter1 = ArrayAdapter.createFromResource(this,R.array.courses,android.R.layout.simple_spinner_item);
        adapter2 = ArrayAdapter.createFromResource(this,R.array.years,android.R.layout.simple_spinner_item);
        adapter3 = ArrayAdapter.createFromResource(this,R.array.semesters,android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Upload_btn = (Button)findViewById(R.id.upload_button);
        Pdf_btn = (Button)findViewById(R.id.pdf_button);
        Qr_generate = (Button)findViewById(R.id.qr_generate);
        Image = (ImageView)findViewById(R.id.qr_image);
        PdfStatus = (TextView)findViewById(R.id.pdf_status);
        //progressBar = (ProgressBar) findViewById(R.id.progressbar);

        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);

        get_id = mDatabase.push().getKey();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+ "selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+ "selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+ "selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadIt(mPdfUri);

                //Toast.makeText(this, "uploaded", Toast.LENGTH_LONG).show();
                 //Toast makeText (Context context, CharSequence text, int duration);
            }
        });

        //for selecting files from the device
        Pdf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
               // galleryIntent.setType("application/pdf");
               // startActivityForResult(galleryIntent, GALLERY_REQUEST);
                getPDF();
            }
        });

        //For generating QR CODE
        Qr_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2Qr = get_id;
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    Image.setImageBitmap(bitmap);
                    Image.setImageURI(mQrCodeUri);

                    //BitmapDrawable drawable = (BitmapDrawable) Image.getDrawable();
                    //Bitmap bitmap1 = drawable.getBitmap();
                    //storeImage(bitmap1);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        mProgress = new ProgressDialog(this);

    }

    private void getPDF() {

        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                PdfStatus.setText("Selected");

                mPdfUri = data.getData();
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void UploadIt(Uri data)
    {
        //progressBar.setVisibility(View.VISIBLE);
        mProgress.setMessage("Uploading, Please Wait...");


        final String Get_Course = spinner1.getSelectedItem().toString();
        final String Get_Year = spinner2.getSelectedItem().toString();
        final String Get_Sem = spinner3.getSelectedItem().toString();

        if (!TextUtils.isEmpty(Get_Course) && !TextUtils.isEmpty(Get_Year) && !TextUtils.isEmpty(Get_Sem))
        {   mProgress.show();
            //String get_id = mDatabase.push().getKey();
            StorageReference filePath = mStorage.child(Constants.STORAGE_PATH_UPLOADS).child(mPdfUri.getLastPathSegment() + ".pdf");
            filePath.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    @SuppressWarnings("VisibleForTests")
                    final String Get_pdf_url = taskSnapshot.getDownloadUrl().toString();

                    Data data = new Data( Get_Course, Get_Year, Get_Sem, get_id, Get_pdf_url);
                    mDatabase.child(get_id).setValue(data);
                    //progressBar.setVisibility(View.GONE);

                    Intent intent = new Intent(UploadActivity.this, FinishActivity.class);
                    startActivity(intent);
                    //Toast.makeText(this, "uploaded", Toast.LENGTH_LONG).show();
                    mProgress.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }

            });




        }else
        {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }

    }
}
